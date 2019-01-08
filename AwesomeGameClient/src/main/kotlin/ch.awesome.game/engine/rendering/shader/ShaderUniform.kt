package ch.awesome.game.engine.rendering.shader

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import org.khronos.webgl.WebGLUniformLocation

abstract class ShaderUniform(val name: String) {

    var location: WebGLUniformLocation? = null

    abstract fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram)
}