package ch.awesome.game.client.rendering.shader

import ch.awesome.game.client.lib.WebGL2RenderingContext
import org.khronos.webgl.WebGLUniformLocation

abstract class ShaderUniform(val name: String) {

    var location: WebGLUniformLocation? = null

    abstract fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram)
}