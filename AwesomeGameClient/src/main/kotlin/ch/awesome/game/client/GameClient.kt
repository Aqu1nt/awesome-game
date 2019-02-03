package ch.awesome.game.client

import ch.awesome.game.client.lib.GameCanvasElement
import ch.awesome.game.client.lib.LockedMouseEvent
import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.client.objects.base.CTerrain
import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.ModelLoader
import ch.awesome.game.client.rendering.loading.wavefront.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.postprocessing.PostProcessor
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.rendering.textures.GUITexture
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.state.input.InputHandler
import ch.awesome.game.client.state.input.PlayerControl
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.common.math.*
import kotlinx.serialization.ImplicitReflectionSerializer
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window
import kotlin.js.Date
import kotlin.js.Promise
import kotlin.math.floor

class GameClient {

    companion object {
        lateinit var instance: GameClient
    }

    private lateinit var renderer: GameRenderer

    private val state = GameState(
            afterNodeCreate = { gameNode ->
                if (gameNode is Renderable) {
                    gameNode.initModels(renderer.gl)
                }
            }
    )

    @ImplicitReflectionSerializer
    val networkClient = NetworkClient(state)

    val inputHandler = InputHandler()

    @ImplicitReflectionSerializer
    val playerControl = PlayerControl(state, networkClient, inputHandler)

//    val physics = AmmoPhysics()

    init {
        instance = this
    }

    @JsName("startGame")
    @ImplicitReflectionSerializer
    fun startGame(canvas: HTMLCanvasElement?) {
        if (canvas != null) {
            val gameCanvas = canvas as GameCanvasElement
            renderer = GameRenderer(gameCanvas, state.camera, state)

            window.addEventListener("mousedown", { event ->
                val mouseEvent = event as LockedMouseEvent
                if(mouseEvent.button.toInt() == 0) gameCanvas.requestPointerLock()
            })

            Promise.all(arrayOf(ModelLoader.loadAllModels(renderer.gl), TextureImageLoader.loadAllTextureImages())).then {

                val sun = Light(Vector3f(10000.0f, 15000.0f, 10000.0f),
                                Vector3f(0.5f, 0.5f, 0.5f),
                                Vector3f(1.0f, 0.0f, 0.0f))

                var lastUpdate = Date.now()

                val postProcessor = PostProcessor(renderer.gl, canvas.width, canvas.height)

//                val gui = GUITexture(ModelCreator.loadTexture(renderer.gl, TextureImageType.PLAYER_ICON))
//                val xScale = inScreenWidth(inPixelWidth(0.1f, canvas.width), canvas.width)
//                val yScale = inScreenHeight(inPixelWidth(0.1f, canvas.width), canvas.height)
//                val xPos = inScreenWidth(-canvas.width.toFloat(), canvas.width) + xScale
//                val yPos = inScreenHeight(canvas.height.toFloat(), canvas.height) - yScale
//                val guiMat = Matrix4f().identity().translate(xPos, yPos, 0.0f).scale(xScale, yScale, 1.0f)

                val gui = GUITexture(ModelCreator.loadTexture(renderer.gl, TextureImageType.HEALTH_BAR))

                val xScale = inScreenWidth(256.0f, canvas.width)
                val yScale = inScreenHeight(52.0f, canvas.height)
                val xPos = inScreenWidth(-canvas.width.toFloat() + 32, canvas.width) + xScale
                val yPos = inScreenHeight(canvas.height.toFloat() - 32, canvas.height) - yScale;

                val guiMat = Matrix4f().identity().translate(xPos, yPos, 0.0f).scale(xScale, yScale, 1.0f)

                val terrain = CTerrain(renderer.gl)
                terrain.x = -CTerrain.SIZE / 2
                terrain.z = -CTerrain.SIZE / 2
                val terrainMat = Matrix4f().identity().translate(terrain.x, -150.0f, terrain.z)

                fun gameLoop(double: Double) {
                    val tpf = 1.0 / 1000.0 * (Date.now() - lastUpdate)
                    state.update(tpf.toFloat())
                    state.calculateWorldMatrix()
//                    physics.detectCollisions()

                    if (state.player != null) state.camera.update(state.player ?: throw IllegalStateException("no player created"))

                    inputHandler.update()
                    playerControl.update()

                    val maxStages = 50.0
                    var x = 0.0f
                    var y = 0.0f
                    if(state.player?.health != null) {
                        val stage = maxStages - (maxStages / (50.0 / state.player?.health!!))
                        y = floor(stage % 19).toFloat() * 13.0f
                        x = floor(stage / 19.0f).toFloat() * 64.0f
                    }

                    postProcessor.beforeRendering()
                    renderer.prepare(*state.getLightSources(), sun)
                    renderer.renderGameNodes(GameNode.allGameNodes(), terrain, terrainMat)
                    postProcessor.process()

                    renderer.guiRenderer.prepare()
//                    renderer.renderGUI(gui, guiMat, 1.0f / (256.0f / x), a, (1.0f / 256.0f) * 64, (1.0f / 256.0f) * 13.0f)
                    renderer.renderGUI(gui, guiMat, 1.0f / (256.0f / x), 1.0f / (256.0f / y), (1.0f / 256.0f) * 64, (1.0f / 256.0f) * 13.0f)
//                    renderer.renderFont("This text looks quite/nlnice but i have to/nlimprove still a lot!", 0.0f, 0.9f)
                    renderer.renderFont("HP: " + state.player?.health, -0.95f, 0.725f)
                    renderer.renderFont("LVL: " + state.player?.level, -0.95f, 0.625f)
                    renderer.guiRenderer.end()

                    lastUpdate = Date.now()
                    window.requestAnimationFrame(::gameLoop)
                }

                networkClient.connect()

                window.requestAnimationFrame(::gameLoop)
            }
        }
        else {
            throw IllegalStateException("Game canvas not found!")
        }
    }
}