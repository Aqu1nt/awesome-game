package ch.awesome.game.client.rendering.shader.gui

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformMatrix4f
import ch.awesome.game.client.rendering.shader.uniforms.UniformVector2f
import ch.awesome.game.client.lib.WebGL2RenderingContext

class GUIShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, guiVertexShaderSource, guiFragmentShaderSource, arrayOf("position")) {

    val uniformModelMatrix = UniformMatrix4f("modelMatrix").apply { uniforms.add(this) }
    val uniformTexCoordsLeftTop = UniformVector2f("texCoordsLeftTop").apply { uniforms.add(this) }
    val uniformTexCoordsRightBottom = UniformVector2f("texCoordsRightBottom").apply { uniforms.add(this) }
}