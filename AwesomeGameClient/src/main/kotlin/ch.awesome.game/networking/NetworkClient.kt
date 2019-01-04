package ch.awesome.game.networking

import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket

object NetworkClient {

    private var webSocket: WebSocket? = null

    private val activeWebSocket: WebSocket
        get() = webSocket ?: throw IllegalStateException("There is no active WebSocket at the moment")

    fun connect() {
        webSocket = WebSocket("ws://localhost:8080/game")
        activeWebSocket.onmessage = { event ->
            val msgEvent = event as MessageEvent
            println(msgEvent.data)
        }
    }
}