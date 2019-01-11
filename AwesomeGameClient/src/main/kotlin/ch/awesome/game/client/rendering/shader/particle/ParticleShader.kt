package ch.awesome.game.client.rendering.shader.particle

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.UniformMatrix4f
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class ParticleShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, particleVertexShaderSource, particleFragmentShaderSource) {

    val uniformProjectionMatrix = UniformMatrix4f("projectionMatrix").apply { uniforms.add(this) }
}