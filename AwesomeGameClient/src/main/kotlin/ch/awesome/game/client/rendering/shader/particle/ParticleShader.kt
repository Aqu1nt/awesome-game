package ch.awesome.game.client.rendering.shader.particle

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.UniformInt
import ch.awesome.game.client.rendering.shader.uniforms.UniformMatrix4f
import ch.awesome.game.client.rendering.shader.uniforms.UniformVector2f
import ch.awesome.game.client.rendering.shader.uniforms.UniformVector4f
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class ParticleShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, particleVertexShaderSource, particleFragmentShaderSource) {

    val uniformModelMatrix = UniformMatrix4f("modelMatrix").apply { uniforms.add(this) }
    val uniformViewMatrix = UniformMatrix4f("viewMatrix").apply { uniforms.add(this) }
    val uniformProjectionMatrix = UniformMatrix4f("projectionMatrix").apply { uniforms.add(this) }
    val uniformModelTexture = UniformInt("modelTexture").apply { uniforms.add(this) }
    val offsetVector = UniformVector2f("offset").apply { uniforms.add(this) }
    val uniformColor = UniformVector4f("color").apply { uniforms.add(this) }
}