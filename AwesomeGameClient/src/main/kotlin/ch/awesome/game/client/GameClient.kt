package ch.awesome.game.client

import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.Light
import ch.awesome.game.client.rendering.loading.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageLoader
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.state.PlayerControl
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.common.math.Vector3f
import kotlinx.serialization.ImplicitReflectionSerializer
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window
import kotlin.js.Date
import kotlin.js.Promise

class GameClient {

    private lateinit var renderer: GameRenderer

    private val camera: Camera = Camera()

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

    @JsName("startGame")
    @ImplicitReflectionSerializer
    fun startGame(canvas: HTMLCanvasElement?) {
        if (canvas != null) {
            renderer = GameRenderer(canvas, camera, state)
            Promise.all(arrayOf(
                    OBJModelLoader.loadAllModels(renderer.gl),
                    TextureImageLoader.loadAllTextureImages())).then {

                val sun = Light(Vector3f(10000.0f, 15000.0f, 10000.0f),
                                Vector3f(0.5f, 0.5f, 0.5f),
                                Vector3f(1.0f, 0.0f, 0.0f))

                var lastUpdate = Date.now()

                fun gameLoop(double: Double) {
                    val tpf = 1.0 / 1000.0 * (Date.now() - lastUpdate)
                    state.update(tpf.toFloat())
                    state.calculateWorldMatrix()

//                        camera.lookAt(state.player?.localPosition?.x ?: 0.0f, state.player?.localPosition?.y?.plus(40.0f) ?: 40.0f,
//                                      state.player?.localPosition?.z ?: 0.0f, 90.0f, 0.0f, 0.0f)
//                    camera.lookAt(state.player?.worldTranslation?.x ?: 0.0f, 40.0f,
//                            state.player?.worldTranslation?.z?.plus(60.0f) ?: 30.0f, 40.0f, 0.0f, 0.0f)

                    camera.set(state.player?.worldTranslation?.x ?: 0.0f, 30.0f,
                               state.player?.worldTranslation?.z?.plus(40.0f) ?: 30.0f, 40.0f, 0.0f, 0.0f)


                    renderer.prepare(*state.getLightSources(), sun)
                    renderer.renderGameNodes(GameNode.allGameNodes())
                    renderer.end()

                    lastUpdate = Date.now()
                    window.requestAnimationFrame(::gameLoop)
                }

                window.requestAnimationFrame(::gameLoop)

                networkClient.connect()
            }
        }
        else {
            throw IllegalStateException("Game canvas not found!")
        }
    }
}