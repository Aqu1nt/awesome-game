package ch.awesome.game.client.rendering.shader.postprocessing

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.lib.WebGL2RenderingContext

class ContrastShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, contrastVertexShaderSource, contrastFragmentShaderSource, arrayOf("position")) {
}