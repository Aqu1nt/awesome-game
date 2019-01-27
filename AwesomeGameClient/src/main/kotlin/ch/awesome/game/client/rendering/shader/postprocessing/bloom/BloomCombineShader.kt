package ch.awesome.game.client.rendering.shader.postprocessing.bloom

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformInt
import ch.awesome.game.client.lib.WebGL2RenderingContext

class BloomCombineShader(val gl: WebGL2RenderingContext):
        ShaderProgram(gl, bloomVertexShaderSource, bloomCombineFragmentShaderSource, arrayOf("position")) {

    val sceneTexture = UniformInt("sceneTexture").apply { uniforms.add(this) }
    val brightTexture = UniformInt("brightTexture").apply { uniforms.add(this) }
}