package ch.awesome.game.engine.rendering

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document

object ModelCreating {

    fun loadModel(gl: WebGL2RenderingContext, vertices: Array<Float>, textureCoords: Array<Float>, indices: Array<Int>): Model {
        val vao = gl.createVertexArray()
        gl.bindVertexArray(vao)

        storeDataInVBO(gl, 0, 3, vertices)
        storeDataInVBO(gl, 1, 2, textureCoords)
        storeDataInIndicesBuffer(gl, indices.map { it.toShort() }.toTypedArray())

        return Model(vao, indices.size)
    }

    private fun storeDataInVBO(gl: WebGL2RenderingContext, attribute: Int, coordSize: Int, data: Array<Float>) {
        val vbo = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vbo)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(data), WebGLRenderingContext.STATIC_DRAW)
        gl.vertexAttribPointer(attribute, coordSize, WebGLRenderingContext.FLOAT, false,
                         coordSize * Float32Array.BYTES_PER_ELEMENT, 0)
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, null)
    }

    private fun storeDataInIndicesBuffer(gl: WebGL2RenderingContext, indices: Array<Short>) {
        val vbo = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, vbo)
        gl.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, Uint16Array(indices), WebGLRenderingContext.STATIC_DRAW)
    }

    fun loadTexture(gl: WebGL2RenderingContext, image: String): WebGLTexture {
        val texture = gl.createTexture() as WebGLTexture

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)

        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA,
                      WebGLRenderingContext.UNSIGNED_BYTE, document.getElementById(image) as HTMLImageElement)

        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE)

        gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST_MIPMAP_LINEAR)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST_MIPMAP_LINEAR)

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null)

        return texture
    }
}