package ch.awesome.game.client.rendering

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.rendering.shader.particle.ParticleShader
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector4f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector4f
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
    private val particleShader = ParticleShader(gl)

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

        gl.disable(WebGLRenderingContext.CULL_FACE) // TODO: change to enable when particles are fixed
        gl.cullFace(WebGLRenderingContext.BACK)

        viewMatrix.identity()
        projectionMatrix.projectionMatrix(70.0f, canvas.width, canvas.height, 0.1f, 1000.0f)

        // Init model shader
        modelShader.findAllUniformLocations()
        modelShader.start()
        modelShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        modelShader.uniformModelTexture.load(gl, 0)
        modelShader.uniformLightMap.load(gl, 1)
        modelShader.stop()

        // Init particle shader
        particleShader.start()
        particleShader.findAllUniformLocations()
        particleShader.uniformProjectionMatrix.load(gl, projectionMatrix)
        particleShader.uniformModelTexture.load(gl, 0)
        particleShader.stop()
    }

    fun prepare(vararg lights: Light) {
        gl.clearColor(state.scene?.clearColor?.x ?: 1f, state.scene?.clearColor?.y ?: 1f, state.scene?.clearColor?.z ?: 1f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)

        val sortedLights = lights.sortedBy { light ->
            light.position.distance(camera.position)
        }

        this.lights.clear()
        this.lights.addAll(sortedLights)
    }

    fun renderParticle(model: TexturedModel, modelMatrix: Matrix4f, color: IVector4f = Vector4f(1f, 1f, 1f, 1f)) {
        useShader(particleShader)

        particleShader.uniformColor.load(gl, Vector4f(1f, 0f, 1f, 1f))

        gl.disable(WebGLRenderingContext.DEPTH_TEST)
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE)
        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)

        particleShader.uniformColor.load(gl, color)
        particleShader.uniformModelMatrix.load(gl, modelMatrix)

        viewMatrix.viewMatrix(camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
        particleShader.uniformViewMatrix.load(gl, viewMatrix)

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.modelTexture)
        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.bindVertexArray(null)
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)
    }

    fun renderModel(model: TexturedModel, modelMatrix: Matrix4f) {
        useShader(modelShader)

        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)
        gl.enableVertexAttribArray(2)

        modelShader.uniformModelMatrix.load(gl, modelMatrix)

        viewMatrix.viewMatrix(camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
        modelShader.uniformViewMatrix.load(gl, viewMatrix)

        for(i in 0 until maxLights) {
            if(i < lights.size) {
                modelShader.uniformLightPos.load(gl, lights[i].position.x, lights[i].position.y, lights[i].position.z, i)
                modelShader.uniformLightColor.load(gl, lights[i].color.x, lights[i].color.y, lights[i].color.z, i)
                modelShader.uniformLightAttenuation.load(gl, lights[i].attenuation.x, lights[i].attenuation.y, lights[i].attenuation.z, i)
            } else {
                modelShader.uniformLightPos.load(gl, 0.0f, 0.0f, 0.0f, i)
                modelShader.uniformLightColor.load(gl, 0.0f, 0.0f, 0.0f, i)
                modelShader.uniformLightAttenuation.load(gl, 1.0f, 1.0f, 1.0f, i)
            }
        }

        modelShader.uniformReflectivity.load(gl, model.texture.reflectivity)
        modelShader.uniformShineDamper.load(gl, model.texture.shineDamper)

        modelShader.uniformAmbientLight.load(gl, state.scene?.ambientLight ?: 0f)
        modelShader.uniformDirectionalLightPos.load(gl, 0.0f, 1.0f, 1.0f)
        modelShader.uniformDirectionalLightColor.load(gl, 1.0f, 0.0f, 0.0f)

        modelShader.uniformUseLightMap.load(gl, model.texture.lightMap != null)

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.modelTexture)

        if(model.texture.lightMap != null) {
            gl.activeTexture(WebGLRenderingContext.TEXTURE1)
            gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.lightMap)
        }

        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.disableVertexAttribArray(2)
        gl.bindVertexArray(null)
    }

    private fun useShader(shader: ShaderProgram) {
        if (activeShader != shader) {
            activeShader?.stop()
        }
        shader.start()
        activeShader = shader
    }

    fun end() {
        activeShader?.stop()
    }
}