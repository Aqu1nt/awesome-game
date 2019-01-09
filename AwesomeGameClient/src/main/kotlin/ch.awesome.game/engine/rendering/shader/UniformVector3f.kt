package ch.awesome.game.engine.rendering.shader

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.utils.IVector3f
import ch.awesome.game.utils.Vector3f

class UniformVector3f(name: String): ShaderUniform(name) {

    var vec: IVector3f? = null

    fun load(gl: WebGL2RenderingContext, x: Float, y: Float, z: Float) {
        if(vec?.x == x || vec?.y == y || vec?.z == z) return
        if(vec == null) vec = Vector3f(0.0f, 0.0f, 0.0f)

        vec?.x = x
        vec?.y = y
        vec?.z = z

        gl.uniform3f(location, x, y, z)
    }

    fun load(gl: WebGL2RenderingContext, vec: Vector3f) {
        load(gl, vec.x, vec.y, vec.z)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}