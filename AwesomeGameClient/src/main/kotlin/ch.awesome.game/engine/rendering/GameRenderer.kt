package ch.awesome.game.engine.rendering

import ch.awesome.game.engine.rendering.shader.ShaderProgram
import ch.awesome.game.engine.rendering.shader.model.ModelShader
import ch.awesome.game.engine.rendering.shader.model.modelFragmentShaderSource
import ch.awesome.game.engine.rendering.shader.model.modelVertexShaderSource
import ch.awesome.game.lib.glmatrix.GLMatrix
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window
import kotlin.math.cos
import kotlin.math.sin

class GameRenderer (canvas: HTMLCanvasElement) {

    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    var modelMatrix = Matrix4f()
    var viewMatrix = Matrix4f()
    var projectionMatrix = Matrix4f()

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

    fun prepare(vararg lights: Light) {
        shader.start()
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)

        this.lights.clear()
        for(l in lights) {
            this.lights.add(l)
        }
    }

    fun render(model: TexturedModel, x: Float, y: Float, z: Float) {
        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)
        gl.enableVertexAttribArray(2)

        Matrix4f.identity(modelMatrix)
        Matrix4f.translate(modelMatrix, x, y, z)
        shader.uniformModelMatrix.load(gl, modelMatrix)

        val angle = 0f
        val dirX = sin(Matrix4f.toRadians(angle))
        val dirZ = cos(Matrix4f.toRadians(angle))
        val posX = dirX * 30.0f
        val posZ = dirZ * 30.0f

        Matrix4f.identity(viewMatrix)
        GLMatrix.mat4.lookAt(viewMatrix.floatArray, arrayOf(posX, 30.0f, posZ), arrayOf(0.0f, 0.0f, 0.0f), arrayOf(0.0f, 1.0f, 0.0f))
        //Matrix4f.translate(viewMatrix, 0.0f, 0.0f, 0.0f)
        //Matrix4f.rotate(viewMatrix, window.performance.now().toFloat() / 100.0f, arrayOf(0.0f, 1.0f, 0.0f))
        shader.uniformViewMatrix.load(gl, viewMatrix)

        for(i in 0 until maxLights) {
            if(i < lights.size) {
                shader.uniformLightPos.load(gl, lights[i].position.x, lights[i].position.y, lights[i].position.z, i)
                shader.uniformLightColor.load(gl, lights[i].color.x, lights[i].color.y, lights[i].color.z, i)
            } else {
                shader.uniformLightPos.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightColor.load(gl, 0.0f, 0.0f, 0.0f, i)
            }
        }

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