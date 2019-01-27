package ch.awesome.game.client.rendering.shader.postprocessing.gaussianblur

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformFloat
import ch.awesome.game.client.lib.WebGL2RenderingContext

class HorizontalBlurShader(val gl: WebGL2RenderingContext):
        ShaderProgram(gl, horizontalBlurVertexShaderSource, blurFragmentShaderSource, arrayOf("position")) {

    val targetWidth = UniformFloat("targetWidth").apply { uniforms.add(this) }
}