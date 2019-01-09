package ch.awesome.game

import ch.awesome.game.engine.rendering.*
import ch.awesome.game.networking.NetworkClient
import ch.awesome.game.state.GameState
import ch.awesome.game.state.PlayerControl
import ch.awesome.game.state.interfaces.Renderable
import ch.awesome.game.utils.Vector3f
import kotlinx.serialization.ImplicitReflectionSerializer
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

class GameClient {

    private lateinit var renderer: GameRenderer
    private lateinit var camera: Camera

    private val state = GameState(
        afterNodeCreate = { gameNode ->
            if (gameNode is Renderable) {
                gameNode.initModels(renderer.gl)
            }
        }
    )

    @ImplicitReflectionSerializer
    private val networkClient = NetworkClient(state)

    @ImplicitReflectionSerializer
    private val playerControl = PlayerControl(state, networkClient)

    @ImplicitReflectionSerializer
    fun startGame() {
        TextureImageLoader.loadAllTextureImages().then {
            val canvas = document.getElementById("game-canvas") as HTMLCanvasElement?
            if (canvas != null) {
                renderer = GameRenderer(canvas)
                camera = Camera()

                OBJModelLoader.loadAllModels(renderer.gl).then {
                    var loop: (Double) -> Unit = {}
                    var lastUpdate = Date.now()
                    loop = {
                        val tpf = 1.0 / 1000.0 * (Date.now() - lastUpdate)
                        state.update(tpf.toFloat())

                        camera.position.x = state.player?.localPosition?.x ?: 0.0f
                        camera.position.y = state.player?.localPosition?.y?.plus(40.0f) ?: 40.0f
                        camera.position.z = state.player?.localPosition?.z ?: 0.0f
                        camera.pitch = 90.0f

                        renderer.prepare(camera, *state.getLightSources())
                        state.render(renderer)
                        renderer.end()

                        lastUpdate = Date.now()
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
