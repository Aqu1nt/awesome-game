package ch.awesome.game.client.state

import ch.awesome.game.common.network.events.PlayerDirectionChange
import ch.awesome.game.common.network.events.PlayerDirectionChangeNetworkEvent
import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.common.math.IVector3f
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.math.roundToInt

@ImplicitReflectionSerializer
class PlayerControl(state: GameState,
                    networkClient: NetworkClient) {

    init {
        val eventListener: (Event) -> Unit = { event ->
            val keyEvent = event as KeyboardEvent
            val directionChangeEvent = when (keyEvent.key) {
                "d" -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        payload = PlayerDirectionChange(x = 1f, y = 0f, z = 0f)
                    }
                }
                "w" -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        payload = PlayerDirectionChange(x = 0f, y = 0f, z = -1f)
                    }
                }
                "a" -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        payload = PlayerDirectionChange(x = -1f, y = 0f, z = 0f)
                    }

                }
                "s" -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        payload = PlayerDirectionChange(x = 0f, y = 0f, z = 1f)
                    }
                }
                " " -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        payload = PlayerDirectionChange(x = 0f, y = 0f, z = 0f)
                    }
                }
                else -> {
                    null
                }
            }

            if (directionChangeEvent != null) {
                window.setTimeout({
                    state.player?.velocity = object : IVector3f {
                        override var x: Float = directionChangeEvent.payload!!.x
                        override var y: Float = directionChangeEvent.payload!!.y
                        override var z: Float = directionChangeEvent.payload!!.z
                    }
                }, (networkClient.pingToServer / 2f).roundToInt())

                networkClient.sendEvent(directionChangeEvent, PlayerDirectionChangeNetworkEvent::class.serializer())
            }
        }
        window.addEventListener("keydown", eventListener)
    }
}
