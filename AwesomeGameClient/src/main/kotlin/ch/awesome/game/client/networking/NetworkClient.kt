package ch.awesome.game.client.networking

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import ch.awesome.game.common.network.events.IStateChangesNetworkEvent
import ch.awesome.game.common.network.events.IStateNetworkEvent
import ch.awesome.game.common.network.events.PingNetworkEvent
import ch.awesome.game.common.network.events.PlayerJoinedGameNetworkEvent
import ch.awesome.game.client.state.GameState
import ch.awesome.game.common.utils.ISmartChange
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import kotlin.browser.window
import kotlin.js.Date

@ImplicitReflectionSerializer
class NetworkClient(private val state: GameState) {

    var pingToServer: Float = 0f

    private var webSocket: WebSocket? = null

    private val activeWebSocket: WebSocket
        get() = webSocket ?: throw IllegalStateException("There is no active WebSocket at the moment")

    private var pingInterval: Int? = null

    fun connect() {
        webSocket?.close()
        webSocket = WebSocket("ws://${window.location.hostname}:8080/game")

        activeWebSocket.onopen = {
            sendPing()
        }

        activeWebSocket.onmessage = { event ->
            val msgEvent = event as MessageEvent
            val networkEvent = JSON.parse<INetworkEvent<*>>(msgEvent.data as String)
            handleNetworkEvent(networkEvent)
        }

        pingInterval?.let { pingInterval -> window.clearInterval(pingInterval) }
        pingInterval = window.setInterval(this::sendPing, 1_000)
    }

    fun <T : INetworkEvent<*>> sendEvent(event: T, encoder: KSerializer<T>) {
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
            NetworkEventType.PING               -> {
                val pingEvent = event.unsafeCast<PingNetworkEvent>()
                pingToServer = (Date.now() - (pingEvent.payload.unsafeCast<Number?>()?.toDouble() ?: Date.now())).toFloat()
            }
            else                                                               -> {
                throw IllegalStateException("Unknown network event type ${event.type}!")
            }
        }
    }

    private fun sendPing() {
        sendEvent(PingNetworkEvent().apply { payload = Date.now().toLong() }, PingNetworkEvent::class.serializer())
    }
}