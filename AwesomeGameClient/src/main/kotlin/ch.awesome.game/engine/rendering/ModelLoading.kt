package ch.awesome.game.engine.rendering

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.lib.webgl2.WebGLVertexArrayObject
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document

object ModelLoading {

    fun loadModel(gl: WebGL2RenderingContext, vertices: Array<Float>, textureCoords: Array<Float>, indices: Array<Int>) {
        val vao = gl.createVertexArray()
        gl.bindVertexArray(vao)
    }

    fun storeData(gl: WebGL2RenderingContext, attribute: Int, coordSize: Int, data: Array<Float>) {

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