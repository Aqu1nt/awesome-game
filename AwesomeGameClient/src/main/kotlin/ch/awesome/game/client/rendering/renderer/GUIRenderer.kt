package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.GUITexture
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.shader.gui.GUIShader
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import org.khronos.webgl.WebGLRenderingContext

class GUIRenderer(val gl: WebGL2RenderingContext, val shader: GUIShader) {

    private val positions = arrayOf(-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f)
    val model = ModelCreator.loadModel(gl, positions, 2)

    fun prepare() {
        shader.start()

        gl.enable(WebGLRenderingContext.BLEND)
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA)
        gl.disable(WebGLRenderingContext.DEPTH_TEST)
    }

    fun render(texture: GUITexture, modelMatrix: Matrix4f) {
        gl.bindVertexArray(model.vao)
        gl.enableVertexAttribArray(0)

        shader.uniformModelMatrix.load(gl, modelMatrix)

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture.texture)

        gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, 0, model.vertexCount)

        gl.disableVertexAttribArray(0)
        gl.bindVertexArray(null)
    }

    fun end() {
        gl.enable(WebGLRenderingContext.DEPTH_TEST)
        gl.disable(WebGLRenderingContext.BLEND)

        shader.stop()
    }
}