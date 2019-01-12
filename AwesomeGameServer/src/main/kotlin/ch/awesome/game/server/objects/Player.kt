package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.common.objects.IPlayer
import ch.awesome.game.server.instance.GAME
import ch.awesome.game.server.network.GameWebSocketHandler
import ch.awesome.game.server.objects.base.MovingBaseObject
import ch.awesome.game.server.utils.withSmartProperties
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.math.cos
import kotlin.math.sin

class Player: MovingBaseObject(20f), IPlayer<Vector3f> {

    var health = 50.0f

    @JsonIgnore
    var webSocketHandler: GameWebSocketHandler? = null

    init {
        withSmartProperties()
    }

    fun sendEvent(event: NetworkEvent<*>) {
        webSocketHandler?.sendEvent(event)
    }

    override fun update(tpf: Float) {
        if (velocity.x > 0.0f) rotation.y = -90.0f
        if (velocity.x < 0.0f) rotation.y = 90.0f
        if (velocity.z > 0.0f) rotation.y = 0.0f
        if (velocity.z < 0.0f) rotation.y = 180.0f

        scale = Vector3f(health, health, health) / 10.0f

        super.update(tpf)
    }

    fun shoot() {
        val b = Bullet(this)
        b.position = worldTranslation

        b.velocity = when(rotation.y) {
            0.0f -> Vector3f(0.0f, 0.0f, 1.0f)
            90.0f -> Vector3f(-1.0f, 0.0f, 0.0f)
            180.0f -> Vector3f(0.0f, 0.0f, -1.0f)
            -90.0f -> Vector3f(1.0f, 0.0f, 0.0f)
            else -> Vector3f(0.0f, 0.0f, 0.0f)
        }

        b.unitPerSecond = 40.0f

        GAME.world.addChild(b)
    }
}