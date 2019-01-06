package ch.awesome.game

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.engine.rendering.TexuteImageLoader
import ch.awesome.game.networking.NetworkClient
import ch.awesome.game.state.GameState
import ch.awesome.game.state.interfaces.Renderable
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

class GameClient {

    private lateinit var renderer: GameRenderer

    private val state = GameState(
        afterNodeCreate = { gameNode ->
            if (gameNode is Renderable) {
                gameNode.initModels(renderer.gl)
            }
        }
    )

    private val networkClient = NetworkClient(state)

    fun startGame() {
        TexuteImageLoader.loadAllTextureImages().then {
            networkClient.connect()

            val canvas = document.getElementById("game-canvas") as HTMLCanvasElement?
            if (canvas != null) {
                renderer = GameRenderer(canvas)

                var loop: (Double) -> Unit = {}
                loop = {
                    renderer.clear()
                    state.render(renderer)
                    window.requestAnimationFrame (loop)
                }
                window.requestAnimationFrame (loop)
            }
            else {
                throw IllegalStateException("Game canvas not found!")
            }
        }
    }
}
