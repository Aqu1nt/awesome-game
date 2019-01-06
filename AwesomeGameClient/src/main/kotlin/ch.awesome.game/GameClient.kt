package ch.awesome.game

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.engine.rendering.Light
import ch.awesome.game.engine.rendering.OBJModelLoader
import ch.awesome.game.engine.rendering.TextureImageLoader
import ch.awesome.game.networking.NetworkClient
import ch.awesome.game.state.GameState
import ch.awesome.game.state.PlayerControl
import ch.awesome.game.state.interfaces.Renderable
import ch.awesome.game.utils.Vector3f
import kotlinx.serialization.ImplicitReflectionSerializer
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

    @ImplicitReflectionSerializer
    private val playerControl = PlayerControl(state, networkClient)

    fun startGame() {
        TextureImageLoader.loadAllTextureImages().then {
            val canvas = document.getElementById("game-canvas") as HTMLCanvasElement?
            if (canvas != null) {
                renderer = GameRenderer(canvas)

                OBJModelLoader.loadAllModels(renderer.gl).then {
                    var loop: (Double) -> Unit = {}
                    loop = {
                        renderer.prepare(Light(Vector3f(10.0f, 5.0f, 10.0f), Vector3f(1.0f, 0.9f, 0.4f)))
                        state.render(renderer)
                        renderer.end()
                        window.requestAnimationFrame (loop)
                    }
                    window.requestAnimationFrame (loop)

                    networkClient.connect()
                }

            }
            else {
                throw IllegalStateException("Game canvas not found!")
            }
        }
    }
}
