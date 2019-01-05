package ch.awesome.game

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.networking.NetworkClient
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

class GameClient {

    fun startGame() {
        NetworkClient.connect()

        val canvas = document.getElementById("game-canvas") as HTMLCanvasElement?
        if (canvas != null) {
            val renderer = GameRenderer(canvas)

            var loop: (Double) -> Unit = {}
            loop = {
                renderer.render()
                window.requestAnimationFrame (loop)
            }
            window.requestAnimationFrame (loop)
        }
        else {
            throw IllegalStateException("Game canvas not found!")
        }
    }
}
