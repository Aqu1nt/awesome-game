package ch.awesome.game.client.rendering

import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window

class GameRenderer (canvas: HTMLCanvasElement) {

    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    var modelMatrix = Matrix4f()
    var viewMatrix = Matrix4f()
    var projectionMatrix = Matrix4f()

    lateinit var camera: Camera
    var lights = mutableListOf<Light>()

    val shader = ModelShader(gl)

    companion object {
        // is set to one in order test light selection
        val maxLights = 16
    }

    init {
        canvas.width = window.innerWidth
        canvas.height = window.innerHeight

        gl.viewport(0, 0, canvas.width, canvas.height)
        gl.clearColor(0.2f, 0.5f, 0.8f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)
        gl.enable(WebGLRenderingContext.CULL_FACE)
        gl.cullFace(WebGLRenderingContext.BACK)

        shader.start()

        Matrix4f.identity(modelMatrix)
        Matrix4f.identity(viewMatrix)
        Matrix4f.projectionMatrix(projectionMatrix, 70.0f, canvas.width, canvas.height, 0.1f, 1000.0f)

        shader.findAllUniformLocations()
        shader.uniformProjectionMatrix.load(gl, projectionMatrix)

        shader.stop()
    }

    fun prepare(camera: Camera, vararg lights: Light) {
        shader.start()
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)

        this.camera = camera
        this.lights.clear()
        for(l in lights) {
            this.lights.add(l)
        }
    }

    fun render(model: TexturedModel, modelMatrix: Matrix4f) {
        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)
        gl.enableVertexAttribArray(2)

        shader.uniformModelMatrix.load(gl, modelMatrix)

        Matrix4f.viewMatrix(viewMatrix, camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
        shader.uniformViewMatrix.load(gl, viewMatrix)

        val sortedLights = lights.sortedBy { light ->
            light.position.distance(camera.position)
        }

        for(i in 0 until maxLights) {
            if(i < sortedLights.size) {
                shader.uniformLightPos.load(gl, sortedLights[i].position.x, sortedLights[i].position.y, sortedLights[i].position.z, i)
                shader.uniformLightColor.load(gl, sortedLights[i].color.x, sortedLights[i].color.y, sortedLights[i].color.z, i)
                shader.uniformLightAttenuation.load(gl, sortedLights[i].attenuation.x, sortedLights[i].attenuation.y, sortedLights[i].attenuation.z, i)
            } else {
                shader.uniformLightPos.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightColor.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightAttenuation.load(gl, 1.0f, 1.0f, 1.0f, i)
            }
        }

        shader.uniformReflectivity.load(gl, model.texture.reflectivity)
        shader.uniformShineDamper.load(gl, model.texture.shineDamper)

        shader.uniformAmbientLight.load(gl, 0.3f)

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.texture)
        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.disableVertexAttribArray(2)
        gl.bindVertexArray(null)
    }

    fun end() {
        shader.stop()
    }
}