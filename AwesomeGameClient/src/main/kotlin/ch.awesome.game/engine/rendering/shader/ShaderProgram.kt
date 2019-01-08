package ch.awesome.game.engine.rendering.shader

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext

@Suppress("LeakingThis")
open class ShaderProgram(private val gl: WebGL2RenderingContext, private val vertexShaderSource: String,
                         private val fragmentShaderSource: String) {

    var program: WebGLProgram? = null
    protected val uniforms= mutableListOf<ShaderUniform>()

    init {
        val vertexShader = gl.createShader(WebGLRenderingContext.VERTEX_SHADER)
        val fragmentShader = gl.createShader(WebGLRenderingContext.FRAGMENT_SHADER)

        gl.shaderSource(vertexShader, vertexShaderSource)
        gl.shaderSource(fragmentShader, fragmentShaderSource)

        gl.compileShader(vertexShader)
        if (gl.getShaderParameter(vertexShader, WebGLRenderingContext.COMPILE_STATUS) == false) {
            throw IllegalStateException("Couldn't compile vertex shader: " + gl.getShaderInfoLog(vertexShader))
        }

        gl.compileShader(fragmentShader)
        if (gl.getShaderParameter(fragmentShader, WebGLRenderingContext.COMPILE_STATUS) == false) {
            throw IllegalStateException("Couldn't compile fragment shader: " + gl.getShaderInfoLog(fragmentShader))
        }

        program = gl.createProgram()
        gl.attachShader(program, vertexShader)
        gl.attachShader(program, fragmentShader)

        gl.bindAttribLocation(program, 0, "position")
        gl.bindAttribLocation(program, 1, "textureCoords")
        gl.bindAttribLocation(program, 2, "normal")

        gl.linkProgram(program)
        if (gl.getProgramParameter(program, WebGLRenderingContext.LINK_STATUS) == false) {
            throw IllegalStateException("Couldn't link shader program: " + gl.getProgramInfoLog(program))
        }
    }

    fun findAllUniformLocations() {
        for(u in uniforms) {
            u.findLocation(gl, this)
        }
    }

    fun start() {
        gl.useProgram(program)
    }

    fun stop() {
        gl.useProgram(null)
    }
}