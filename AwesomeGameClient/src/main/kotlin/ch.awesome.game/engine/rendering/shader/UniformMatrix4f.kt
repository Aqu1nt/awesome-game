package ch.awesome.game.engine.rendering.shader

import ch.awesome.game.engine.rendering.Matrix4f
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.utils.IVector3f
import ch.awesome.game.utils.Vector3f

class UniformMatrix4f(name: String): ShaderUniform(name) {

    fun load(gl: WebGL2RenderingContext, mat: Matrix4f) {
        gl.uniformMatrix4fv(location, false, mat.floatArray)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}