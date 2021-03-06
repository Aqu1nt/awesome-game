package ch.awesome.game.client.rendering.shader.skybox

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformMatrix4f
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.shader.uniforms.UniformVector3f

class SkyboxShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, skyboxVertexShaderSource, skyboxFragmentShaderSource,
        arrayOf("position")) {

    val uniformViewMatrix = UniformMatrix4f("viewMatrix").apply { uniforms.add(this) }
    val uniformProjectionMatrix = UniformMatrix4f("projectionMatrix").apply { uniforms.add(this) }
    val uniformSkyColor = UniformVector3f("skyColor").apply { uniforms.add(this) }
}