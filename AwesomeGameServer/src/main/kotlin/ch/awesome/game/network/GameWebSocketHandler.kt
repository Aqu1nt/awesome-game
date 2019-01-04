package ch.awesome.game.network

import ch.awesome.game.objects.Armor
import ch.awesome.game.objects.Player
import ch.awesome.game.objects.World
import ch.awesome.game.utils.Vector3f
import ch.awesome.game.utils.toJSON
import org.springframework.web.socket.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class GameWebSocketHandler: WebSocketHandler {

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        fun println(string: String) {
            session.sendMessage(TextMessage(string))
        }

        thread {
            val world = World()

            val armor1 = Armor()
            val armor2 = Armor()
            val armor3 = Armor()

            val player = Player().apply {
                addChild(armor1)
                addChild(armor2)
                addChild(armor3)
            }

            player.age = 5
            player.name = "Emil"
            armor1.name = "armor 1!"
            armor3.name = "Armor 3"

            world.addChild(player)

            thread {
                Thread.sleep(2500)
                player.position += Vector3f(10f, -5f, 3f)
                Thread.sleep(2500)
                player.position += Vector3f(10f, -5f, 3f)
                player.removeChild(armor1)
                Thread.sleep(2500)
                player.position += Vector3f(10f, -5f, 3f)
                Thread.sleep(2500)
                world.removeChild(player)
            }

            thread {
                repeat(((1000f * 60f) * 15f).toInt()) {
                    println(String(world.fetchAndResetChanges().toJSON()))
                    sleep((1000f / 60f).toLong())
                }
            }
        }
    }

    override fun supportsPartialMessages(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}