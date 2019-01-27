package ch.awesome.game.client.rendering.shader.uniforms

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.ShaderUniform
import ch.awesome.game.client.lib.WebGL2RenderingContext

class UniformBoolean(name: String): ShaderUniform(name) {

    var value: Boolean? = null

    fun load(gl: WebGL2RenderingContext, value: Boolean) {
        if(this.value == value) return

        this.value = value
        gl.uniform1f(location, if(value) 1.0f else 0.0f)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}