package ch.awesome.game.server.network

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import ch.awesome.game.common.network.events.*
import ch.awesome.game.server.instance.GAME
import ch.awesome.game.server.objects.Player
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.Executors

class GameWebSocketHandler : TextWebSocketHandler() {

    private val sendExecutor = Executors.newSingleThreadExecutor()
    private var session: WebSocketSession? = null
    private var player: Player? = null

    companion object {
        private val objectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val event = objectMapper.readValue<NetworkEvent<*>>(message.asBytes(), NetworkEvent::class.java)

        when (event.type) {
            NetworkEventType.PLAYER_DIRECTION_CHANGE -> {
                val playerDirectionChangeEvent = objectMapper.convertValue(event, PlayerDirectionChangeNetworkEvent::class.java)
                player?.let { player ->
                    player.velocity = player.velocity.copy(
                            x = playerDirectionChangeEvent.payload!!.x,
                            y = playerDirectionChangeEvent.payload!!.y,
                            z = playerDirectionChangeEvent.payload!!.z
                    )
                }
            }
            NetworkEventType.PING                    -> {
                val pingNetworkEvent = objectMapper.convertValue(event, PingNetworkEvent::class.java)
                sendEvent(pingNetworkEvent)
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        this.session = session
        GAME.join().thenAccept { player ->
            this.player = player
            player.webSocketHandler = this

            // Send join event
            sendEvent(PlayerJoinedGameNetworkEvent(PlayerJoinedGameInfo(
                    playerId = player.id
            )))

            // Send initial state
            sendEvent(StateNetworkEvent(GAME.world.state()))
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        player?.let { GAME.leave(it) }
        sendExecutor.shutdown()
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        player?.let { GAME.leave(it) }
        sendExecutor.shutdown()
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }

    fun sendEvent(event: INetworkEvent<*>) {
        sendExecutor.submit {
            val networkEventString = objectMapper.writeValueAsString(event)
            if (session?.isOpen == true) {
                session?.sendMessage(TextMessage(networkEventString))
            }
        }
    }
}