package ch.awesome.game.client.rendering.postprocessing

import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.*

class FrameBufferObject(val gl: WebGL2RenderingContext, private val width: Int = 64, private val height: Int = 64) {

    // TODO: depth texture only available in lib, add this later
//    companion object {
//        val DEPTH_BUFFER_NONE = 0
//        val DEPTH_BUFFER_TEXTURE = 1
//        val DEPTH_BUFFER_RENDER_BUFFER = 2
//    }

    var frameBuffer: WebGLFramebuffer? = null

    var colorBuffer: WebGLBuffer? = null
    var colorTexture: WebGLTexture? = null

    var depthBuffer: WebGLRenderbuffer? = null

    init {
        createFrameBuffer()
        createColorTextureAttachment()
        createDepthBuffer()
    }

    fun createFrameBuffer() {
        frameBuffer = gl.createFramebuffer()
        gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, frameBuffer)
    }

    fun createColorTextureAttachment() {
        colorTexture = gl.createTexture()

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, colorTexture)
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGL2RenderingContext.RGBA8, width, height, 0, WebGLRenderingContext.RGBA,
                WebGLRenderingContext.UNSIGNED_BYTE, null)

        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE)

        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.LINEAR)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.LINEAR)

        gl.framebufferTexture2D(WebGLRenderingContext.FRAMEBUFFER, WebGLRenderingContext.COLOR_ATTACHMENT0,
                WebGLRenderingContext.TEXTURE_2D, colorTexture, 0)
    }

    fun createDepthBuffer() {
        depthBuffer = gl.createRenderbuffer()

        gl.bindRenderbuffer(WebGLRenderingContext.RENDERBUFFER, depthBuffer)
        gl.renderbufferStorage(WebGLRenderingContext.RENDERBUFFER, WebGL2RenderingContext.DEPTH_COMPONENT24, width, height)
        gl.framebufferRenderbuffer(WebGLRenderingContext.FRAMEBUFFER, WebGLRenderingContext.DEPTH_ATTACHMENT,
                WebGLRenderingContext.RENDERBUFFER, depthBuffer)
    }

//    private fun createMultisampleColorAttachment() {
//        colorBuffer = gl.createRenderbuffer()
//
//        gl.bindRenderbuffer(WebGLRenderingContext.RENDERBUFFER, colorBuffer)
//        gl.renderbufferStorageMultisample(WebGLRenderingContext.RENDERBUFFER, 4, WebGL2RenderingContext.RGBA8, width, height)
//        gl.framebufferRenderbuffer(WebGLRenderingContext.FRAMEBUFFER, WebGLRenderingContext.COLOR_ATTACHMENT0,
//                WebGLRenderingContext.RENDERBUFFER, colorBuffer)
//    }

    fun bindToDraw() {
        gl.bindFramebuffer(WebGL2RenderingContext.DRAW_FRAMEBUFFER, frameBuffer)
        gl.viewport(0, 0, width, height)
    }

    fun bindToRead() {
        gl.bindFramebuffer(WebGL2RenderingContext.READ_FRAMEBUFFER, frameBuffer)
        gl.readBuffer(WebGLRenderingContext.COLOR_ATTACHMENT0)
    }

    fun unbind(canvasWidth: Int, canvasHeight: Int) {
        gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, null)
        gl.viewport(0, 0, canvasWidth, canvasHeight)
    }

//    fun resolve(output: FrameBufferObject, canvasWidth: Int, canvasHeight: Int) {
//        gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, output.frameBuffer)
//        gl.blitFramebuffer(0, 0, width, height, 0, 0, output.width, output.height,
//                WebGLRenderingContext.COLOR_BUFFER_BIT, WebGLRenderingContext.NEAREST)
//
//        // TODO: when adding the depth buffer, add also the depth buffer bit
//
//        unbind(canvasWidth, canvasHeight)
//    }
//
//    fun resolve(canvasWidth: Int, canvasHeight: Int) {
//        gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, null)
//        gl.drawBuffers(arrayOf(WebGLRenderingContext.BACK))
//
//        gl.blitFramebuffer(0, 0, width, height, 0, 0, canvasWidth, canvasHeight,
//                WebGLRenderingContext.COLOR_BUFFER_BIT, WebGLRenderingContext.NEAREST)
//
//        unbind(canvasWidth, canvasHeight)
//    }
}