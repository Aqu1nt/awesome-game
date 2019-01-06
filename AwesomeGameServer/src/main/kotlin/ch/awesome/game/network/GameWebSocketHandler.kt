package ch.awesome.game.network

import ch.awesome.game.instance.GAME
import ch.awesome.game.network.events.StateNetworkEvent
import ch.awesome.game.objects.Player
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.socket.*
import java.util.concurrent.Executors

class GameWebSocketHandler : WebSocketHandler {

    private val sendExecutor = Executors.newSingleThreadExecutor()
    private var session: WebSocketSession? = null
    private var player: Player? = null

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {

    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        this.session = session
        GAME.join().thenAccept { player ->
            this.player = player
            player.webSocketHandler = this
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

    fun sendEvent(event: NetworkEvent<*>) {
        sendExecutor.submit {
            val networkEventString = objectMapper.writeValueAsString(event)
            if (session?.isOpen == true) {
                session?.sendMessage(TextMessage(networkEventString))
            }
        }
    }
}