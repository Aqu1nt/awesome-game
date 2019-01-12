package ch.awesome.game.client.rendering.shader.uniforms

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.ShaderUniform
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector2f
import ch.awesome.game.common.math.Vector2f

class UniformVector2f(name: String): ShaderUniform(name) {

    private var vec: IVector2f? = null

    fun load(gl: WebGL2RenderingContext, x: Float, y: Float) {
        if(vec?.x == x && vec?.y == y) return
        if(vec == null) vec = Vector2f(0.0f, 0.0f)

        vec?.x = x
        vec?.y = y

        gl.uniform2f(location, x, y)
    }

    fun load(gl: WebGL2RenderingContext, vec: IVector2f) {
        load(gl, vec.x, vec.y)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        location = gl.getUniformLocation(shader.program, name)
    }
}