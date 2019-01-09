package ch.awesome.game.client

import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.OBJModelLoader
import ch.awesome.game.client.rendering.TextureImageLoader
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.state.PlayerControl
import ch.awesome.game.client.state.interfaces.Renderable
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
