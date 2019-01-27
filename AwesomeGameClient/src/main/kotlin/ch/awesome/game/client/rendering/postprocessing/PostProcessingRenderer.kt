package ch.awesome.game.client.rendering.postprocessing

import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture

class PostProcessingRenderer(private val gl: WebGL2RenderingContext, private val width: Int = 0, private val height: Int = 0) {

    var fbo: FrameBufferObject? = null

    init {
        if (width != 0 && height != 0) {
            fbo = FrameBufferObject(gl, width, height)
        }
    }

    fun render(displayWidth: Int, displayHeight: Int) {
        if (fbo != null) fbo?.bindToDraw()

        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
        gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, 0, 4)

        if (fbo != null) fbo?.unbind(displayWidth, displayHeight)
    }

    fun getOutputTexture(): WebGLTexture {
        return fbo?.colorTexture!!
    }

}