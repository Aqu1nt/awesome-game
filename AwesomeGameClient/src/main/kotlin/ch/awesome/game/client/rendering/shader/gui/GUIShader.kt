package ch.awesome.game.client.rendering.shader.gui

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformMatrix4f
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class GUIShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, guiVertexShaderSource, guiFragmentShaderSource, arrayOf("position")) {

    val uniformModelMatrix = UniformMatrix4f("modelMatrix").apply { uniforms.add(this) }
}