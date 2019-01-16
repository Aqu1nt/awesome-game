package ch.awesome.game.client.webgl2

import org.khronos.webgl.WebGLRenderingContext

abstract external class WebGL2RenderingContext: WebGLRenderingContext {

    fun createVertexArray(): WebGLVertexArrayObject
    fun deleteVertexArray(vao: WebGLVertexArrayObject)
    fun isVertexArray(vao: WebGLVertexArrayObject): Boolean
    fun bindVertexArray(vao: WebGLVertexArrayObject?)

    companion object {
        val TEXTURE_CUBE_MAP: Int
        val TEXTURE_CUBE_MAP_POSITIVE_X: Int
    }
}