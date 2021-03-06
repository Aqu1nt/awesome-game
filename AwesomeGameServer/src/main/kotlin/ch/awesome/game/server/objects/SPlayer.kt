package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.common.objects.IPlayer
import ch.awesome.game.server.instance.GAME
import ch.awesome.game.server.network.GameWebSocketHandler
import ch.awesome.game.server.objects.base.SMovingBaseObject
import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.server.utils.withSmartProperties
import com.bulletphysics.collision.shapes.BoxShape
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import com.fasterxml.jackson.annotation.JsonIgnore

class SPlayer: SMovingBaseObject(20f), IPlayer<Vector3f> {

    companion object {
        private val GRAVITY = -30.0f
    }

    var health: Float by SmartProperty(50.0f)
    var level: Float by SmartProperty(0.0f)
    var animation: String by SmartProperty("idle")

    private var upwardsSpeed = 0.0f

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
        animation = if (unitPerSecond == 0.0f) {
            "idle"
        } else {
            "swimming"
        }

//        if (level >= 2.0f) {
//            upwardsSpeed += GRAVITY * tpf
//            position.y += upwardsSpeed * tpf
//
//            val terrainY = -10.0f
//
//            if (position.y <= terrainY) {
//                upwardsSpeed = 0.0f
//            }
//        }

        super.update(tpf)
    }

    override fun afterUpdate() {
        physicsBody?.let { physicsBody ->
            physicsBody.getWorldTransform(physicsTransform)
            physicsTransform.setFromOpenGLMatrix(worldMatrix.floatArray.toFloatArray())
            physicsBody.setWorldTransform(physicsTransform)
        }

        super.afterUpdate()
    }

    fun shoot() {
        val b = SBullet(this)
        b.position = worldTranslation
        b.velocity = velocity
        b.unitPerSecond = 40.0f

        GAME.world.addChild(b)
    }

    fun cheat(name: String) {
        if (name == "levelup") {
            level ++
        }
    }
}