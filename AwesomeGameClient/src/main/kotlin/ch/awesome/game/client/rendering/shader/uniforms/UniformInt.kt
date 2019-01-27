package ch.awesome.game.client.rendering.shader.uniforms

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.ShaderUniform
import ch.awesome.game.client.lib.WebGL2RenderingContext

class UniformInt(name: String): ShaderUniform(name) {

    var value: Int? = null

    fun load(gl: WebGL2RenderingContext, value: Int) {
        if(this.value == value) return

        this.value = value
        gl.uniform1i(location, value)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}