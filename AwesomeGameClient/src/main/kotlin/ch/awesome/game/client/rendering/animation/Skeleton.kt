package ch.awesome.game.client.rendering.animation

import ch.awesome.game.common.math.Matrix4f

class Skeleton(val joints: MutableList<Joint>, val rootJoint: Joint) {

    init {
        rootJoint.calculateInverseBindTransform(Matrix4f().identity())
    }

    fun getJoint(name: String): Joint? {
        for (joint in joints) {
            if (joint.name == name) {
                return joint
            }
        }

        return null
    }

    fun getJointTransforms(): Array<Matrix4f> {
        val jointMatrices = Array(10) { Matrix4f() }
        addJointsToArray(rootJoint, jointMatrices)

        return jointMatrices
    }

    private fun addJointsToArray(joint: Joint, jointMatrices: Array<Matrix4f>) {
        jointMatrices[joint.id] = joint.animatedTransform
        for (child in joint.children) {
            addJointsToArray(child, jointMatrices)
        }
    }
}