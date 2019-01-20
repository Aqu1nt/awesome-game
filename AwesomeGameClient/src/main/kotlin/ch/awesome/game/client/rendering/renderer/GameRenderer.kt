package ch.awesome.game.client.rendering

import ch.awesome.game.client.rendering.renderer.GUIRenderer
import ch.awesome.game.client.rendering.renderer.ModelRenderer
import ch.awesome.game.client.rendering.renderer.ParticleRenderer
import ch.awesome.game.client.rendering.renderer.SkyboxRenderer
import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.gui.GUIShader
import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.rendering.shader.particle.ParticleShader
import ch.awesome.game.client.rendering.shader.skybox.SkyboxShader
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector4f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector4f
import ch.awesome.game.common.math.toRadians
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window

class GameRenderer (canvas: HTMLCanvasElement,
                    private val camera: Camera,
                    private val state: GameState) {

    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    private var viewMatrix = Matrix4f()
    private var projectionMatrix = Matrix4f()

    private var lights = mutableListOf<Light>()

    private var activeShader: ShaderProgram? = null

    private val modelShader = ModelShader(gl)
    private val modelRenderer = ModelRenderer(gl, modelShader, camera)

    private val particleShader = ParticleShader(gl)
    private val particleRenderer = ParticleRenderer(gl, particleShader, camera)

    private val skyboxShader = SkyboxShader(gl)
    private val skyboxRenderer by lazy { SkyboxRenderer(gl, skyboxShader, camera) }

    private val guiShader = GUIShader(gl)
    private val guiRenderer = GUIRenderer(gl, guiShader)

    companion object {
        // is set to one in order test light selection
        val maxLights = 16
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

        projectionMatrix.projectionMatrix(70.0f, canvas.width, canvas.height, 0.1f, 1000.0f)

        modelShader.findAllUniformLocations()
        modelShader.start()
        modelShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        modelShader.uniformModelTexture.load(gl, 0)
        modelShader.uniformLightMap.load(gl, 1)
        modelShader.stop()

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

    fun renderGameNodes(nodes: Collection<GameNode>) {
        modelRenderer.prepare()
        for(n in nodes) {
            if (n is Renderable) {
                n.render(this)
            }
        }
        modelRenderer.end()

        skyboxRenderer.prepare()
        skyboxRenderer.render()
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
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)

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

    fun renderParticle(model: TexturedModel, modelMatrix: Matrix4f, color: IVector4f = Vector4f(1f, 1f, 1f, 1f)) {
        particleRenderer.renderParticle(model, modelMatrix, color, viewMatrix)
    }

    fun renderGUI(gui: GUITexture, modelMatrix: Matrix4f) {
        guiRenderer.prepare()
        guiRenderer.render(gui, modelMatrix)
        guiRenderer.end()
    }

    fun end() {

    }
}