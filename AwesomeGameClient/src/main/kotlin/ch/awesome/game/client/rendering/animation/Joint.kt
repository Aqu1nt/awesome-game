package ch.awesome.game.client.rendering.animation

import ch.awesome.game.common.math.Matrix4f

class Joint(val id: Int, val name: String, val localBindTransform: Matrix4f) {

    var parent: Joint? = null
    val children = mutableListOf<Joint>()

    var transform = JointTransform()
    var animatedTransform = Matrix4f().identity()

    var bindTransform = Matrix4f().identity()
    var inverseBindTransform = Matrix4f().identity()

    var animated = true

    fun addChild(child: Joint) {
        children.add(child)
    }

    fun createAnimatedTransform(parentTransform: Matrix4f?, toChildren: Boolean) {
        parentTransform?.multiply(transform.getLocalTransform(), animatedTransform)

        if (toChildren) {
            for (child in children) {
                child.createAnimatedTransform(animatedTransform, child.animated)
            }
        }

        animatedTransform.multiply(inverseBindTransform, animatedTransform)
    }

    fun calculateInverseBindTransform(parentBindTransform: Matrix4f) {
        parentBindTransform.multiply(localBindTransform, bindTransform)
        bindTransform.invert(inverseBindTransform)

        for (child in children) {
            child.calculateInverseBindTransform(bindTransform)
        }
    }
}