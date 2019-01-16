package ch.awesome.game.client.state

import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.math.toRadians
import ch.awesome.game.common.network.events.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@ImplicitReflectionSerializer
class PlayerControl(state: GameState,
                    networkClient: NetworkClient) {

    init {
        val eventListener: (Event) -> Unit = { event ->
            val keyEvent = event as KeyboardEvent
            val networkEvent = when (keyEvent.key) {
                "w" -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(unitPerSec = 20.0f)
                    }
                }
                "s" -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(unitPerSec = -20.0f)
                    }
                }
                "d" -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        val angle = state.player!!.rotation.y + 10.0f

                        val x = sin(toRadians(angle - 180.0f))
                        val z = -cos(toRadians(angle - 180.0f))
                        val vec = Vector3f(x, 0.0f, z)

                        payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
                    }
                }
                "a" -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        val angle = state.player!!.rotation.y - 10.0f

                        val x = sin(toRadians(angle - 180.0f))
                        val z = -cos(toRadians(angle - 180.0f))
                        val vec = Vector3f(x, 0.0f, z)

                        payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
                    }
                }
                " " -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(0.0f)
                    }
                }
                "p" -> {
                    PlayerShootNetworkEvent()
                }
                else -> {
                    null
                }
            }

            if (networkEvent != null) {
                when (networkEvent) {
                    is PlayerDirectionChangeNetworkEvent -> {
                        window.setTimeout({
                            state.player?.velocity = object : IVector3f {
                                override var x: Float = networkEvent.payload!!.x
                                override var y: Float = networkEvent.payload!!.y
                                override var z: Float = networkEvent.payload!!.z
                            }
                        }, (networkClient.pingToServer / 2f).roundToInt())
                        networkClient.sendEvent(networkEvent, PlayerDirectionChangeNetworkEvent::class.serializer())
                    }
                    is PlayerSpeedChangeNetworkEvent -> {
                        networkClient.sendEvent(networkEvent, PlayerSpeedChangeNetworkEvent::class.serializer())
                    }
                    is PlayerShootNetworkEvent -> {
                        networkClient.sendEvent(networkEvent, PlayerShootNetworkEvent::class.serializer())
                    }
                }
            }
        }
        window.addEventListener("keydown", eventListener)
    }
}
