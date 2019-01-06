package ch.awesome.game.lib.webgl2

import org.khronos.webgl.WebGLRenderingContext

abstract external class WebGL2RenderingContext: WebGLRenderingContext {

    fun createVertexArray(): WebGLVertexArrayObject
    fun deleteVertexArray(vao: WebGLVertexArrayObject)
    fun isVertexArray(vao: WebGLVertexArrayObject): Boolean
    fun bindVertexArray(vao: WebGLVertexArrayObject?)
}