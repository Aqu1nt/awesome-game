package ch.awesome.game.client.rendering.shader.uniforms

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.ShaderUniform
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector4f
import ch.awesome.game.common.math.Vector4f

class UniformVector4f(name: String): ShaderUniform(name) {

    private var vec: IVector4f? = null

    fun load(gl: WebGL2RenderingContext, x: Float, y: Float, z: Float, w: Float) {
        if(vec?.x == x && vec?.y == y && vec?.z == z && vec?.w == w) return
        if(vec == null) vec = Vector4f(0.0f, 0.0f, 0.0f, 0.0f)

        vec?.x = x
        vec?.y = y
        vec?.z = z
        vec?.w = w

        gl.uniform4f(location, x, y, z, w)
    }

    fun load(gl: WebGL2RenderingContext, vec: IVector4f) {
        load(gl, vec.x, vec.y, vec.z, vec.w)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}