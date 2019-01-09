package ch.awesome.game.client.rendering.shader

import ch.awesome.game.client.webgl2.WebGL2RenderingContext

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