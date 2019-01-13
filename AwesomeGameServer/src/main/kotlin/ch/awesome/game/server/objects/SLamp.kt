package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.ILamp
import ch.awesome.game.server.objects.base.SBaseObject
import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.server.utils.withSmartProperties
import com.bulletphysics.collision.shapes.CylinderShape
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import javax.vecmath.Quat4f

class SLamp: SBaseObject(), ILamp<Vector3f> {

    override var color: Vector3f by SmartProperty(Vector3f(1.0f, 1.0f, 1.0f))

    private var physicsBody: RigidBody? = null
    private var physicsTransform = Transform()

    init { withSmartProperties() }

    override fun afterAdd() {
        physicsBody = RigidBody(1f,
                DefaultMotionState(physicsTransform),
                CylinderShape(javax.vecmath.Vector3f(0.75f, 7.5f, 0.75f))
        )
        physicsBody!!.setDamping(0f, 1f)
        physicsBody!!.userPointer = this
        game.physics.dynamicsWorld.addRigidBody(physicsBody)
    }

    override fun afterRemove() {
        game.physics.dynamicsWorld.removeRigidBody(physicsBody)
    }

    override fun afterUpdate() {
        physicsBody?.let { physicsBody ->
            physicsBody.getWorldTransform(physicsTransform)
            physicsTransform.setRotation(Quat4f(worldRotation.x, worldRotation.y, worldRotation.z, worldRotation.w))
            physicsTransform.origin.set(worldTranslation.x, worldTranslation.y, worldTranslation.z)
            physicsBody.setWorldTransform(physicsTransform)
        }
        super.afterUpdate()
    }
}