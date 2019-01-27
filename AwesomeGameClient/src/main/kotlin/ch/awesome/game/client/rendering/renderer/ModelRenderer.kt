package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.Light
import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.rendering.shader.model.ModelShader
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import org.khronos.webgl.WebGLRenderingContext

class ModelRenderer(val gl: WebGL2RenderingContext, val shader: ModelShader, val camera: Camera) {

    fun prepare() {
        shader.start()
    }

    fun render(model: TexturedModel, modelMatrix: Matrix4f, viewMatrix: Matrix4f, state: GameState, lights: MutableList<Light>) {
        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)
        gl.enableVertexAttribArray(2)

        shader.uniformModelMatrix.load(gl, modelMatrix)
        shader.uniformViewMatrix.load(gl, viewMatrix)

        for(i in 0 until GameRenderer.maxLights) {
            if(i < lights.size) {
                shader.uniformLightPos.load(gl, lights[i].position.x, lights[i].position.y, lights[i].position.z, i)
                shader.uniformLightColor.load(gl, lights[i].color.x, lights[i].color.y, lights[i].color.z, i)
                shader.uniformLightAttenuation.load(gl, lights[i].attenuation.x, lights[i].attenuation.y, lights[i].attenuation.z, i)
            } else {
                shader.uniformLightPos.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightColor.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightAttenuation.load(gl, 1.0f, 1.0f, 1.0f, i)
            }
        }

        shader.uniformReflectivity.load(gl, model.texture.reflectivity)
        shader.uniformShineDamper.load(gl, model.texture.shineDamper)

        shader.uniformAmbientLight.load(gl, state.scene?.ambientLight ?: 0f)
        shader.uniformDirectionalLightPos.load(gl, 0.0f, 1.0f, 1.0f)
        shader.uniformDirectionalLightColor.load(gl, 1.0f, 0.0f, 0.0f)

        shader.uniformUseLightMap.load(gl, model.texture.lightMap != null)

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.modelTexture)

        if(model.texture.lightMap != null) {
            gl.activeTexture(WebGLRenderingContext.TEXTURE1)
            gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture.lightMap)
        }

        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.disableVertexAttribArray(2)
        gl.bindVertexArray(null)
    }

    fun end() {
        shader.stop()
    }
}