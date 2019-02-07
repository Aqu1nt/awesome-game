package ch.awesome.game.client.rendering.animation

import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Quaternion
import ch.awesome.game.common.math.Vector3f

class JointTransform(var position: Vector3f = Vector3f(0.0f, 0.0f, 0.0f), var rotation: Quaternion = Quaternion(0.0f, 0.0f, 0.0f, 1.0f)) {

    fun getLocalTransform(): Matrix4f {
        val mat = Matrix4f().identity()
        mat.translate(position)
        mat.fromQuaternion(rotation)

        return mat
    }

    companion object {
        fun interpolate(start: JointTransform, end: JointTransform, progression: Float): JointTransform {
            val pos = start.position.interpolate(end.position, progression)
            val rot = start.rotation.interpolate(end.rotation, progression)

            return JointTransform(pos, rot)
        }
    }
}