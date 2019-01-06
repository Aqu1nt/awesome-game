package ch.awesome.game.networking

import ch.awesome.game.network.INetworkEvent
import ch.awesome.game.network.NetworkEventType
import ch.awesome.game.network.events.IStateChangesNetworkEvent
import ch.awesome.game.network.events.IStateNetworkEvent
import ch.awesome.game.network.events.PlayerJoinedGameNetworkEvent
import ch.awesome.game.state.GameState
import ch.awesome.game.utils.ISmartChange
import kotlinx.serialization.KSerializer
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket

class NetworkClient(private val state: GameState) {

    private var webSocket: WebSocket? = null

    private val activeWebSocket: WebSocket
        get() = webSocket ?: throw IllegalStateException("There is no active WebSocket at the moment")

    fun connect() {
        webSocket = WebSocket("ws://localhost:8080/game")
        activeWebSocket.onmessage = { event ->
            val msgEvent = event as MessageEvent
            val networkEvent = JSON.parse<INetworkEvent<*>>(msgEvent.data as String)
            handleNetworkEvent(networkEvent)
        }
    }

    fun <T: INetworkEvent<*>>sendEvent(event: T, encoder: KSerializer<T>) {
        activeWebSocket.send(kotlinx.serialization.json.JSON.stringify(encoder, event))
    }

    private fun handleNetworkEvent(event: INetworkEvent<*>) {
        when (NetworkEventType.valueOf(event.type.toString())) {
            NetworkEventType.STATE              -> {
                val stateEvent = event.unsafeCast<IStateNetworkEvent>()
                state.replaceState(stateEvent.payload)
            }
            NetworkEventType.STATE_CHANGES      -> {
                val stateChangeEvent = event.unsafeCast<IStateChangesNetworkEvent>()
                val changes = stateChangeEvent.payload.unsafeCast<Array<ISmartChange>>().toList()
                state.applyChanges(changes)
            }
            NetworkEventType.PLAYER_JOINED_GAME -> {
                val playerJoinedEvent = event.unsafeCast<PlayerJoinedGameNetworkEvent>()
                state.playerId = playerJoinedEvent.payload.playerId
            }
            else                                -> {
                throw IllegalStateException("Unknown network event type ${event.type}!")
            }
        }
    }
}