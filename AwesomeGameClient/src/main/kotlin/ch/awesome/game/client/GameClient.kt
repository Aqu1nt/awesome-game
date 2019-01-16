package ch.awesome.game.client

import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.wavefront.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.state.PlayerControl
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.common.math.Matrix4f
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

                val gui = GUITexture(ModelCreator.loadTexture(renderer.gl, TextureImageType.GRASS))
                val guiMat = Matrix4f().identity().translate(-0.5f, 0.5f, 0.0f).scale(0.25f, 0.25f, 1.0f)

                fun gameLoop(double: Double) {
                    val tpf = 1.0 / 1000.0 * (Date.now() - lastUpdate)
                    state.update(tpf.toFloat())
                    state.calculateWorldMatrix()

                    camera.set(state.player?.worldTranslation?.x ?: 0.0f, 25f,
                               state.player?.worldTranslation?.z?.plus(40.0f) ?: 40.0f, 20.0f, 0.0f, 0.0f)


                    renderer.prepare(*state.getLightSources(), sun)
                    renderer.renderGameNodes(GameNode.allGameNodes())
                    renderer.renderGUI(gui, guiMat)
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