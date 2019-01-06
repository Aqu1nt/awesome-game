package ch.awesome.game.state

import ch.awesome.game.network.events.PlayerDirectionChange
import ch.awesome.game.network.events.PlayerDirectionChangeNetworkEvent
import ch.awesome.game.networking.NetworkClient
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window

@ImplicitReflectionSerializer
class PlayerControl(state: GameState,
                    networkClient: NetworkClient) {

    init {
        window.addEventListener("keydown", { event ->
            val keyEvent = event as KeyboardEvent
            when (keyEvent.key) {
                "d" -> {
                    networkClient.sendEvent(
                            PlayerDirectionChangeNetworkEvent().apply {
                                payload = PlayerDirectionChange(x = 1f, y = 0f, z = 0f)
                            },
                            PlayerDirectionChangeNetworkEvent::class.serializer()
                    )
                }
                "w" -> {
                    networkClient.sendEvent(
                            PlayerDirectionChangeNetworkEvent().apply {
                                payload = PlayerDirectionChange(x = 0f, y = 0f, z = -1f)
                            },
                            PlayerDirectionChangeNetworkEvent::class.serializer()
                    )
                }
                "a" -> {
                    networkClient.sendEvent(
                            PlayerDirectionChangeNetworkEvent().apply {
                                payload = PlayerDirectionChange(x = -1f, y = 0f, z = 0f)
                            },
                            PlayerDirectionChangeNetworkEvent::class.serializer()
                    )
                }
                "s" -> {
                    networkClient.sendEvent(
                            PlayerDirectionChangeNetworkEvent().apply {
                                payload = PlayerDirectionChange(x = 0f, y = 0f, z = 1f)
                            },
                            PlayerDirectionChangeNetworkEvent::class.serializer()
                    )
                }
                " " -> {
                    networkClient.sendEvent(
                            PlayerDirectionChangeNetworkEvent().apply {
                                payload = PlayerDirectionChange(x = 0f, y = 0f, z = 0f)
                            },
                            PlayerDirectionChangeNetworkEvent::class.serializer()
                    )
                }
            }
        })
    }
}
