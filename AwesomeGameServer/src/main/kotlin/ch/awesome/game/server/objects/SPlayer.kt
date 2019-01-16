package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.common.objects.IPlayer
import ch.awesome.game.server.instance.GAME
import ch.awesome.game.server.network.GameWebSocketHandler
import ch.awesome.game.server.objects.base.SMovingBaseObject
import ch.awesome.game.server.utils.withSmartProperties
import com.bulletphysics.collision.shapes.BoxShape
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.vecmath.Quat4f

class SPlayer: SMovingBaseObject(0f), IPlayer<Vector3f> {

    var health = 50.0f

    @JsonIgnore
    var webSocketHandler: GameWebSocketHandler? = null

    private var physicsBody: RigidBody? = null
    private var physicsTransform = Transform()

    init {
        withSmartProperties()
    }

    override fun afterAdd() {
        physicsBody = RigidBody(1f,
                DefaultMotionState(physicsTransform),
                BoxShape(javax.vecmath.Vector3f(0.75f, 0.75f, 1f))
        )
        physicsBody!!.setDamping(0f, 1f)
        physicsBody!!.userPointer = this
        game.physics.dynamicsWorld.addRigidBody(physicsBody)
    }

    override fun afterRemove() {
        game.physics.dynamicsWorld.removeRigidBody(physicsBody)
    }

    fun sendEvent(event: NetworkEvent<*>) {
        webSocketHandler?.sendEvent(event)
    }

    override fun update(tpf: Float) {
        super.velocity = Vector3f(0f, 0f, 1f)
        super.update(tpf)
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

    fun shoot() {
        val b = SBullet(this)
        b.position = worldTranslation
        b.velocity = Vector3f(1.0f, 0.0f, 0.0f)
        b.unitPerSecond = 40.0f

        GAME.world.addChild(b)
    }
}