package ch.awesome.game.engine.rendering.shader

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.utils.IVector3f
import ch.awesome.game.utils.Vector3f

class UniformFloat(name: String): ShaderUniform(name) {

    var value: Float? = null

    fun load(gl: WebGL2RenderingContext, value: Float) {
        if(this.value == value) return

       this.value = value
        gl.uniform1f(location, value)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}