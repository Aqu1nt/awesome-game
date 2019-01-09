package ch.awesome.game.client.rendering.shader

import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Vector3f

class UniformVector3fArray(name: String, val size: Int): ShaderUniform(name) {

    private var uniforms = Array<UniformVector3f>(size) { UniformVector3f("$name[$it]") }

    fun load(gl: WebGL2RenderingContext, x: Float, y: Float, z: Float, index: Int) {
        uniforms[index].load(gl, x, y, z)
    }

    fun load(gl: WebGL2RenderingContext, vec: Vector3f, index: Int) {
        uniforms[index].load(gl, vec)
    }

    override fun findLocation(gl: WebGL2RenderingContext, shader: ShaderProgram) {
        for(u in uniforms) {
            u.findLocation(gl, shader)
        }
    }
}