package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.rendering.shader.skybox.SkyboxShader
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLTexture
import kotlin.browser.window

class SkyboxRenderer(val gl: WebGL2RenderingContext, val shader: SkyboxShader, val camera: Camera) {

    val SIZE = 500.0f
    val vertices = arrayOf(
        -SIZE, SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        -SIZE,  SIZE, -SIZE,
        SIZE,  SIZE, -SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
        SIZE, -SIZE,  SIZE
    )

    val textures = arrayOf(TextureImageType.SKYBOX_RIGHT, TextureImageType.SKYBOX_LEFT, TextureImageType.SKYBOX_TOP,
            TextureImageType.SKYBOX_BOTTOM, TextureImageType.SKYBOX_BACK, TextureImageType.SKYBOX_FRONT)

    val model = ModelCreator.loadModel(gl, vertices, 3)
    val texture = ModelCreator.loadCubeMap(gl, textures)

    fun prepare() {
        shader.start()
    }

    fun render() {
        gl.bindVertexArray(model.vao)
        gl.enableVertexAttribArray(0)

        val viewMatrix = Matrix4f().viewMatrix(camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
        viewMatrix.m30 = 0.0f
        viewMatrix.m31 = 0.0f
        viewMatrix.m32 = 0.0f
        viewMatrix.rotate(window.performance.now().toFloat() / 500.0f, Matrix4f.Y_AXIS)
        shader.uniformViewMatrix.load(gl, viewMatrix)

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGL2RenderingContext.TEXTURE_CUBE_MAP, texture)

        gl.drawArrays(WebGLRenderingContext.TRIANGLES, 0, model.vertexCount)

        gl.disableVertexAttribArray(0)
        gl.bindVertexArray(null)
    }

    fun end() {
        shader.stop()
    }
}