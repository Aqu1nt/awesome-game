package ch.awesome.game.client.lib

import org.khronos.webgl.WebGLRenderingContext

abstract external class WebGL2RenderingContext: WebGLRenderingContext {

    fun createVertexArray(): WebGLVertexArrayObject
    fun deleteVertexArray(vao: WebGLVertexArrayObject)
    fun isVertexArray(vao: WebGLVertexArrayObject): Boolean
    fun bindVertexArray(vao: WebGLVertexArrayObject?)

    fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, offset: Int)

    fun drawBuffers(buffers: Array<Int>)
    fun readBuffer(src: Int)
    fun blitFramebuffer(srcX0: Int, srcY0: Int, srcX1: Int, srcY1: Int, dstX0: Int, dstY0: Int, dstX1: Int, dstY1: Int, mask: Int, filter: Int)
    fun renderbufferStorageMultisample(type: Int, samples: Int, filter: Int, width: Int, height: Int)

    companion object {
        val RGBA8: Int
        val DEPTH_COMPONENT24: Int

        val DRAW_FRAMEBUFFER: Int
        val READ_FRAMEBUFFER: Int

        val TEXTURE_CUBE_MAP: Int
        val TEXTURE_CUBE_MAP_POSITIVE_X: Int
    }
}