package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.rendering.shader.particle.ParticleShader
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector4f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector4f
import org.khronos.webgl.WebGLRenderingContext

class ParticleRenderer(val gl: WebGL2RenderingContext, val shader: ParticleShader, val camera: Camera) {

    fun prepare() {
        shader.start()
        gl.disable(WebGLRenderingContext.CULL_FACE)
        gl.depthMask(false)

        gl.enable(WebGLRenderingContext.BLEND)
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE)
    }

    fun renderParticle(model: TexturedModel, modelMatrix: Matrix4f, color: IVector4f = Vector4f(1f, 1f, 1f, 1f), viewMatrix: Matrix4f) {
        shader.uniformColor.load(gl, Vector4f(1f, 0f, 1f, 1f))

        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)

        shader.uniformColor.load(gl, color)
        shader.uniformModelMatrix.load(gl, modelMatrix)

        viewMatrix.viewMatrix(camera.position.x, camera.position.y, camera.position.z, camera.pitch, camera.yaw, camera.roll)
        shader.uniformViewMatrix.load(gl, viewMatrix)

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.modelTexture)
        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.bindVertexArray(null)
    }

    fun end() {
        gl.blendFunc(WebGLRenderingContext.SRC_ALPHA, WebGLRenderingContext.ONE_MINUS_SRC_ALPHA)
        gl.depthMask(true)

        gl.enable(WebGLRenderingContext.CULL_FACE)
        gl.cullFace(WebGLRenderingContext.BACK)

        gl.disable(WebGLRenderingContext.BLEND)

        shader.stop()
    }
}