package ch.awesome.game.client.rendering.animation

import ch.awesome.game.client.rendering.models.AnimatedModel
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Quaternion
import ch.awesome.game.common.math.Vector3f

class Animator(val model: AnimatedModel) {

    companion object {
        val BETWEEN_ANIMATIONS_TIME = 0.3f
    }

    var currentAnimation: Animation? = null
    var currentPose = hashMapOf<String, JointTransform>()
    var animationTime = 0.0f

    var nextAnimation: Animation? = null
    var lastAnimationEnd = hashMapOf<String, JointTransform>()
    var nextAnimationStart = hashMapOf<String, JointTransform>()
    var betweenTime = 0.0f
    var betweenAnimations = false

    fun setAnimation(animation: Animation) {
        if (currentAnimation == animation || nextAnimation == animation) {
            return
        }

        if (currentAnimation != null || nextAnimation != null && (betweenTime == 0.0f || nextAnimation != animation)) {
            currentAnimation = null
            nextAnimation = animation
            betweenAnimations = true
            animationTime = 0.0f
            betweenTime = 0.0f
            lastAnimationEnd = currentPose
            nextAnimationStart = animation.keyFrames[0].pose
        } else {
            currentAnimation = animation
            nextAnimation = null
            animationTime = 0.0f
            betweenTime = 0.0f
        }
    }

    fun setAnimation(name: String) {
        val animation = model.getAnimation(name)
        if (animation != null) {
            setAnimation(animation)
        }
    }

    fun update(tpf: Float) {
        if (currentAnimation != null) {
            animationTime += tpf
            if (animationTime >= currentAnimation!!.length) {
                animationTime %= currentAnimation!!.length
            }

            currentPose = inAnimationPose()
            for (joint in model.skeleton.joints) {
                joint.transform = currentPose[joint.name]!!
            }

            model.skeleton.rootJoint.createAnimatedTransform(Matrix4f().identity(), true)
        } else if (currentAnimation == null && betweenAnimations) {
            betweenTime += tpf

            var progression = betweenTime * (1.0f / BETWEEN_ANIMATIONS_TIME)
            progression *= progression * (3.0f - 2.0f * progression)

            currentPose = interpolatePoses(lastAnimationEnd, nextAnimationStart, progression)
            for (joint in model.skeleton.joints) {
                joint.transform = currentPose[joint.name] ?: JointTransform(Vector3f(), Quaternion())
            }

            model.skeleton.rootJoint.createAnimatedTransform(Matrix4f().identity(), true)

            if (betweenTime >= BETWEEN_ANIMATIONS_TIME) {
                betweenAnimations = false
                betweenTime = 0.0f
                currentAnimation = nextAnimation
            }
        }
    }

    private fun inAnimationPose(): HashMap<String, JointTransform> {
        val frames = previousAndNextKeyFrame()
        val progression = progression(frames[0], frames[1])
        return interpolatePoses(frames[0].pose, frames[1].pose, progression)
    }

    private fun previousAndNextKeyFrame(): Array<AnimationKeyFrame> {
        val allKeyFrames = currentAnimation!!.keyFrames
        var previousKeyFrame = allKeyFrames[0]
        var nextKeyFrame = allKeyFrames[0]

        for (i in 1 until allKeyFrames.size) {
            nextKeyFrame = allKeyFrames[i]
            if (nextKeyFrame.time > animationTime) {
                break
            }
            previousKeyFrame = allKeyFrames[i]
        }

        return arrayOf(previousKeyFrame, nextKeyFrame)
    }

    private fun progression(previous: AnimationKeyFrame, next: AnimationKeyFrame): Float {
        val totalTime = next.time - previous.time
        val time = animationTime - previous.time
        return time / totalTime
    }

    private fun interpolatePoses(previous: HashMap<String, JointTransform>?,
                                 next: HashMap<String, JointTransform>, progression: Float): HashMap<String, JointTransform> {
        val pose = hashMapOf<String, JointTransform>()

        if (previous != null) {
            for (joint in previous.keys) {
                val previousTransform = previous[joint]
                val nextTransform = next[joint]
                val finalTransform = JointTransform.interpolate(previousTransform!!, nextTransform!!, progression)

                pose[joint] = finalTransform
            }
        }

        return pose
    }
}