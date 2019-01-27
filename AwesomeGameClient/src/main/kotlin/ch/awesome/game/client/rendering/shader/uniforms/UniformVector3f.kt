package ch.awesome.game.client.rendering.shader.uniforms

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.ShaderUniform
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f

class UniformVector3f(name: String): ShaderUniform(name) {

    private var vec: IVector3f? = null

    fun load(gl: WebGL2RenderingContext, x: Float, y: Float, z: Float) {
        if(vec?.x == x && vec?.y == y && vec?.z == z) return
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