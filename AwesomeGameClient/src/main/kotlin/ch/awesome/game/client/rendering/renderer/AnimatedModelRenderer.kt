package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.Light
import ch.awesome.game.client.rendering.models.TexturedModel
import ch.awesome.game.client.state.GameState
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.models.AnimatedModel
import ch.awesome.game.client.rendering.shader.animatedmodel.AnimatedModelShader
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector3f
import org.khronos.webgl.WebGLRenderingContext
import kotlin.js.Date

class AnimatedModelRenderer(val gl: WebGL2RenderingContext, val shader: AnimatedModelShader, val camera: Camera) {

    fun prepare() {
        shader.start()
    }

    fun render(model: AnimatedModel, modelMatrix: Matrix4f, viewMatrix: Matrix4f, state: GameState, lights: MutableList<Light>) {
        gl.bindVertexArray(model.texturedModel.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)
        gl.enableVertexAttribArray(2)
        gl.enableVertexAttribArray(3)
        gl.enableVertexAttribArray(4)

        shader.uniformModelMatrix.load(gl, modelMatrix)
        shader.uniformViewMatrix.load(gl, viewMatrix)

        for (i in 0 until GameRenderer.MAX_LIGHTS) {
            if (i < lights.size) {
                shader.uniformLightPos.load(gl, lights[i].position.x, lights[i].position.y, lights[i].position.z, i)
                shader.uniformLightColor.load(gl, lights[i].color.x, lights[i].color.y, lights[i].color.z, i)
                shader.uniformLightAttenuation.load(gl, lights[i].attenuation.x, lights[i].attenuation.y, lights[i].attenuation.z, i)
            } else {
                shader.uniformLightPos.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightColor.load(gl, 0.0f, 0.0f, 0.0f, i)
                shader.uniformLightAttenuation.load(gl, 1.0f, 1.0f, 1.0f, i)
            }
        }

        shader.uniformReflectivity.load(gl, model.texturedModel.texture.reflectivity)
        shader.uniformShineDamper.load(gl, model.texturedModel.texture.shineDamper)

        shader.uniformAmbientLight.load(gl, state.scene?.ambientLight ?: 0f)
        shader.uniformDirectionalLightPos.load(gl, 0.0f, 1.0f, 1.0f)
        shader.uniformDirectionalLightColor.load(gl, 1.0f, 0.0f, 0.0f)
        shader.uniformSkyColor.load(gl, state.scene?.skyColor ?: Vector3f(0.0f, 0.0f, 0.0f))

        shader.uniformUseLightMap.load(gl, model.texturedModel.texture.lightMap != null)

        val jointTransforms = model.skeleton.getJointTransforms()

        for (i in 0 until GameRenderer.MAX_JOINTS) {
            if (i < jointTransforms.size) {
                shader.uniformJointTransforms.load(gl, jointTransforms[i], i)
            } else {
                shader.uniformJointTransforms.load(gl, Matrix4f().identity(), i)
            }
        }

        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texturedModel.texture.modelTexture)

        if (model.texturedModel.texture.lightMap != null) {
            gl.activeTexture(WebGLRenderingContext.TEXTURE1)
            gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texturedModel.texture.lightMap)
        }

        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.texturedModel.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.disableVertexAttribArray(2)
        gl.disableVertexAttribArray(3)
        gl.disableVertexAttribArray(4)
        gl.bindVertexArray(null)
    }

    fun end() {
        shader.stop()
    }
}