package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.objects.base.CTerrain
import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.Light
import ch.awesome.game.client.rendering.models.AnimatedModel
import ch.awesome.game.client.rendering.models.TexturedModel
import ch.awesome.game.client.rendering.shader.animatedmodel.AnimatedModelShader
import ch.awesome.game.client.rendering.shader.gui.GUIShader
import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.rendering.shader.particle.ParticleShader
import ch.awesome.game.client.rendering.shader.skybox.SkyboxShader
import ch.awesome.game.client.rendering.shader.terrain.TerrainShader
import ch.awesome.game.client.rendering.textures.GUITexture
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.common.math.IVector4f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector4f
import ch.awesome.game.common.math.inScreenWidth
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window

class GameRenderer (val canvas: HTMLCanvasElement,
                    private val camera: Camera,
                    private val state: GameState) {

    // We totally need this to to anything
    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    private var viewMatrix = Matrix4f()
    private var projectionMatrix = Matrix4f()

    private var lights = mutableListOf<Light>()

    private val modelShader = ModelShader(gl)
    private val modelRenderer = ModelRenderer(gl, modelShader, camera)

    private val animatedModelShader = AnimatedModelShader(gl)
    private val animatedModelRenderer = AnimatedModelRenderer(gl, animatedModelShader, camera)

    private val terrainShader = TerrainShader(gl)
    private val terrainRenderer = TerrainRenderer(gl, terrainShader, camera)

    private val particleShader = ParticleShader(gl)
    private val particleRenderer = ParticleRenderer(gl, particleShader, camera)

    private val skyboxShader = SkyboxShader(gl)
    private val skyboxRenderer by lazy { SkyboxRenderer(gl, skyboxShader, camera) }

    private val guiShader = GUIShader(gl)
    val guiRenderer = GUIRenderer(gl, guiShader)

    private val fontRenderer = FontRenderer(this)

    companion object {
        val MAX_LIGHTS = 16
        val MAX_JOINTS = 50
        val MAX_WEIGHTS = 3
    }

    init {
        canvas.width = window.innerWidth
        canvas.height = window.innerHeight

        gl.viewport(0, 0, canvas.width, canvas.height)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)

        gl.enable(WebGLRenderingContext.BLEND)
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA)

        gl.enable(WebGLRenderingContext.CULL_FACE)
        gl.cullFace(WebGLRenderingContext.BACK)

        viewMatrix.identity()

        projectionMatrix.projectionMatrix(70.0f, canvas.width, canvas.height, 0.1f, 1600.0f)

        modelShader.findAllUniformLocations()
        modelShader.start()
        modelShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        modelShader.uniformModelTexture.load(gl, 0)
        modelShader.uniformLightMap.load(gl, 1)
        modelShader.stop()

        animatedModelShader.findAllUniformLocations()
        animatedModelShader.start()
        animatedModelShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        animatedModelShader.uniformModelTexture.load(gl, 0)
        animatedModelShader.uniformLightMap.load(gl, 1)
        animatedModelShader.stop()

        terrainShader.findAllUniformLocations()
        terrainShader.start()
        terrainShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        terrainShader.uniformModelTexture.load(gl, 0)
        terrainShader.uniformLightMap.load(gl, 1)
        terrainShader.stop()

        particleShader.start()
        particleShader.findAllUniformLocations()
        particleShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        particleShader.stop()

        skyboxShader.start()
        skyboxShader.findAllUniformLocations()
        skyboxShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        skyboxShader.stop()

        guiShader.start()
        guiShader.findAllUniformLocations()
        guiShader.stop()
    }

    fun renderGameNodes(nodes: Collection<GameNode>, terrain: CTerrain, terrainMat: Matrix4f) {
        modelRenderer.prepare()
        for(n in nodes) {
            if (n is Renderable && !n.animated) {
                n.render(this)
            }
        }
        modelRenderer.end()

        animatedModelRenderer.prepare()
        for(n in nodes) {
            if (n is Renderable && n.animated) {
                n.render(this)
            }
        }
        animatedModelRenderer.end()

        terrainRenderer.prepare()
        renderTerrain(terrain, terrainMat)
        terrainRenderer.end()

        skyboxRenderer.prepare()
        skyboxRenderer.render(state)
        skyboxRenderer.end()

        particleRenderer.prepare()
        for(n in nodes) {
            if (n is Renderable) {
                n.renderParticles(this, camera)
            }
        }
        particleRenderer.end()
    }

    fun prepare(vararg lights: Light) {
        gl.clearColor(state.scene?.clearColor?.x ?: 1f, state.scene?.clearColor?.y ?: 1f, state.scene?.clearColor?.z ?: 1f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
        gl.clear(WebGLRenderingContext.DEPTH_BUFFER_BIT)

        val sortedLights = lights.sortedBy { light ->
            light.position.distance(camera.position)
        }

        this.lights.clear()
        this.lights.addAll(sortedLights)

        viewMatrix.viewMatrix(camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
    }

    fun renderModel(model: TexturedModel, modelMatrix: Matrix4f) {
        modelRenderer.render(model, modelMatrix, viewMatrix, state, lights)
    }

    fun renderAnimatedModel(model: AnimatedModel, modelMatrix: Matrix4f) {
        animatedModelRenderer.render(model, modelMatrix, viewMatrix, state, lights)
    }

    fun renderTerrain(terrain: CTerrain, modelMatrix: Matrix4f) {
        terrainRenderer.render(terrain, modelMatrix, viewMatrix, state, lights)
    }

    fun renderParticle(model: TexturedModel, modelMatrix: Matrix4f, color: IVector4f = Vector4f(1f, 1f, 1f, 1f)) {
        particleRenderer.renderParticle(model, modelMatrix, color, viewMatrix)
    }

    fun renderGUI(gui: GUITexture, modelMatrix: Matrix4f, x: Float, y: Float, w: Float, h: Float) {
        guiRenderer.render(gui, modelMatrix, x, y, x + w, y + h)
    }

    fun renderFont(text: String, x: Float, y: Float) {
        val lines = text.split("/nl")

        for(i in 0 until lines.size) {
            fontRenderer.render(lines[i], x, y - inScreenWidth(i * 144.0f, canvas.width))
        }
    }

    fun end() {

    }
}