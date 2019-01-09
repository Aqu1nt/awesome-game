package ch.awesome.game.client.rendering.shader

import ch.awesome.game.client.rendering.Matrix4f
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class UniformMatrix4f(name: String): ShaderUniform(name) {

    fun load(gl: WebGL2RenderingContext, mat: Matrix4f) {
        gl.uniformMatrix4fv(location, false, mat.floatArray)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}