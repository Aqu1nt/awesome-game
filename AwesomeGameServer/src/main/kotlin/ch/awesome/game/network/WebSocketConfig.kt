package ch.awesome.game.network

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler

@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(gameWebSocketHandler(), "/game")
                .setAllowedOrigins("*")
    }

    @Bean
    fun gameWebSocketHandler(): WebSocketHandler {
        return PerConnectionWebSocketHandler(GameWebSocketHandler::class.java)
    }
}