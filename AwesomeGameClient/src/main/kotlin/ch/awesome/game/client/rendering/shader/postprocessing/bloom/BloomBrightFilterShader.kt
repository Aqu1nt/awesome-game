package ch.awesome.game.client.rendering.shader.postprocessing.bloom

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.lib.WebGL2RenderingContext

class BloomBrightFilterShader(val gl: WebGL2RenderingContext):
        ShaderProgram(gl, bloomVertexShaderSource, bloomBrightFragmentShaderSource, arrayOf("position")) {
}