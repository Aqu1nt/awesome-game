package ch.awesome.game.client.rendering

import ch.awesome.game.client.rendering.loading.TextureImageLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture

object ModelCreator {

    fun loadModel(gl: WebGL2RenderingContext, vertices: Array<Float>, textureCoords: Array<Float>, normals: Array<Float>,
                  indices: Array<Int>): Model {
        val vao = gl.createVertexArray()
        gl.bindVertexArray(vao)

        storeDataInVBO(gl, 0, 3, vertices)
        storeDataInVBO(gl, 1, 2, textureCoords)
        storeDataInVBO(gl, 2, 3, normals)
        storeDataInIndicesBuffer(gl, indices.map { it.toShort() }.toTypedArray())

        return Model(vao, indices.size)
    }

    fun loadModel(gl: WebGL2RenderingContext, vertices: Array<Float>, dimensions: Int): Model {
        val vao = gl.createVertexArray()
        gl.bindVertexArray(vao)
        storeDataInVBO(gl, 0, dimensions, vertices)

        return Model(vao, vertices.size / dimensions)
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

    fun loadTexture(gl: WebGL2RenderingContext, image: TextureImageType): WebGLTexture {
        val texture = gl.createTexture() as WebGLTexture

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)

        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA,
                WebGLRenderingContext.UNSIGNED_BYTE, TextureImageLoader.getTextureImage(image))

        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE)

        gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST_MIPMAP_LINEAR)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST)

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null)

        return texture
    }

    fun loadGUITexture(gl: WebGL2RenderingContext, image: TextureImageType): WebGLTexture {
        val texture = gl.createTexture() as WebGLTexture

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)

        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA,
                WebGLRenderingContext.UNSIGNED_BYTE, TextureImageLoader.getTextureImage(image))

        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE)

        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST)

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null)

        return texture
    }

    fun loadCubeMap(gl: WebGL2RenderingContext, textures: Array<TextureImageType>): WebGLTexture {
        val texture = gl.createTexture() as WebGLTexture

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGL2RenderingContext.TEXTURE_CUBE_MAP, texture)

        for(i in 0 until textures.size) {
            gl.texImage2D(WebGL2RenderingContext.TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, WebGLRenderingContext.RGBA,
                    WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, TextureImageLoader.getTextureImage(textures[i]))
        }

        gl.texParameteri(WebGLRenderingContext.TEXTURE_CUBE_MAP,
                WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_CUBE_MAP,
                WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE)

        gl.generateMipmap(WebGLRenderingContext.TEXTURE_CUBE_MAP)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_CUBE_MAP,
                WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST_MIPMAP_LINEAR)
        gl.texParameteri(WebGLRenderingContext.TEXTURE_CUBE_MAP,
                WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST)

        return texture
    }
}