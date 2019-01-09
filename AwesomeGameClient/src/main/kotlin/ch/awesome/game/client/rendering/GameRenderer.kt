package ch.awesome.game.client.rendering

import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window
import kotlin.math.cos
import kotlin.math.sin

class GameRenderer (canvas: HTMLCanvasElement) {

    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    var modelMatrix = Matrix4f()
    var viewMatrix = Matrix4f()
    var projectionMatrix = Matrix4f()

    lateinit var camera: Camera
    var lights = mutableListOf<Light>()

    val shader = ModelShader(gl)

    companion object {
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

        val angle = 0f
        val dirX = sin(Matrix4f.toRadians(angle))
        val dirZ = cos(Matrix4f.toRadians(angle))
        val posX = dirX * 30.0f
        val posZ = dirZ * 30.0f

        Matrix4f.viewMatrix(viewMatrix, camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
        shader.uniformViewMatrix.load(gl, viewMatrix)

        for(i in 0 until maxLights) {
            if(i < lights.size) {
                shader.uniformLightPos.load(gl, lights[i].position.x, lights[i].position.y, lights[i].position.z, i)
                shader.uniformLightColor.load(gl, lights[i].color.x, lights[i].color.y, lights[i].color.z, i)
                shader.uniformLightAttenuation.load(gl, lights[i].attenuation.x, lights[i].attenuation.y, lights[i].attenuation.z, i)
            } else {
                shader.uniformLightPos.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightColor.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightAttenuation.load(gl, 1.0f, 1.0f, 1.0f, i)
            }
        }

        shader.uniformReflectivity.load(gl, model.texture.reflectivity)
        shader.uniformShineDamper.load(gl, model.texture.shineDamper)

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