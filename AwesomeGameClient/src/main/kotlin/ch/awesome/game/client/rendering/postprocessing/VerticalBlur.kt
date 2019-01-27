package ch.awesome.game.client.rendering.postprocessing

import ch.awesome.game.client.rendering.shader.postprocessing.gaussianblur.VerticalBlurShader
import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture

class VerticalBlur(private val gl: WebGL2RenderingContext, private val displayWidth: Int, private val displayHeight: Int,
                   private val width: Int, private val height: Int) {

    val renderer = PostProcessingRenderer(gl, width, height)
    val shader = VerticalBlurShader(gl)

    init {
        shader.start()
        shader.findAllUniformLocations()
        shader.targetHeight.load(gl, width.toFloat())
        shader.stop()
    }

    fun render(texture: WebGLTexture) {
        shader.start()

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
        renderer.render(displayWidth, displayHeight)

        shader.stop()
    }
}