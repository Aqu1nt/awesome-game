package ch.awesome.game.client.rendering.shader.postprocessing.gaussianblur

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformFloat
import ch.awesome.game.client.lib.WebGL2RenderingContext

class VerticalBlurShader(val gl: WebGL2RenderingContext):
        ShaderProgram(gl, verticalBlurVertexShaderSource, blurFragmentShaderSource, arrayOf("position")) {

    val targetHeight = UniformFloat("targetHeight").apply { uniforms.add(this) }
}