package ch.awesome.game.client.rendering.shader.uniforms

import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.ShaderUniform
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector3f

class UniformMatrix4fArray(name: String, val size: Int): ShaderUniform(name) {

    private var uniforms = Array(size) { UniformMatrix4f("$name[$it]") }

    fun load(gl: WebGL2RenderingContext, mat: Matrix4f, index: Int) {
        uniforms[index].load(gl, mat)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        for(u in uniforms) {
            u.findLocation(gl, shader)
        }
    }
}