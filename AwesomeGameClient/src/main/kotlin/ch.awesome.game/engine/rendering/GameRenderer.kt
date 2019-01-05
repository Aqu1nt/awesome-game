package ch.awesome.game.engine.rendering

import ch.awesome.game.lib.glmatrix.GLMatrix
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import org.khronos.webgl.*
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window
import kotlin.math.PI

val vertexShaderSource = """
#version 300 es
precision mediump float;

in vec3 position;
in vec2 textureCoords;

out vec2 passTextureCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
    passTextureCoords = textureCoords;
}
""".trimIndent()

val fragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 passTextureCoords;

out vec4 outColor;

uniform sampler2D modelTexture;

void main()
{
    outColor = texture(modelTexture, passTextureCoords);
}
""".trimIndent()

class GameRenderer (canvas: HTMLCanvasElement) {

    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    var modelMatrix = Matrix4f()
    var viewMatrix = Matrix4f()
    var projectionMatrix = Matrix4f()

    lateinit var modelMatrixLocation: WebGLUniformLocation

    lateinit var indices: Array<Short>

    lateinit var texture: WebGLTexture

    var identity = Float32Array(16)
    var angle = 0.0f;

    init {
        canvas.width = window.innerWidth
        canvas.height = window.innerHeight

        gl.viewport(0, 0, canvas.width, canvas.height)
        gl.clearColor(0.2f, 0.5f, 0.8f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)

        gl.enable(WebGLRenderingContext.DEPTH_TEST)

        gl.enable(WebGLRenderingContext.CULL_FACE)
        gl.cullFace(WebGLRenderingContext.BACK)

        val shader = ShaderProgram(gl, vertexShaderSource, fragmentShaderSource)

        val vertices = arrayOf(
                -1.0, 1.0, -1.0,
                -1.0, 1.0, 1.0,
                1.0, 1.0, 1.0,
                1.0, 1.0, -1.0,

                // Left
                -1.0, 1.0, 1.0,
                -1.0, -1.0, 1.0,
                -1.0, -1.0, -1.0,
                -1.0, 1.0, -1.0,

                // Right
                1.0, 1.0, 1.0,
                1.0, -1.0, 1.0,
                1.0, -1.0, -1.0,
                1.0, 1.0, -1.0,

                // Front
                1.0, 1.0, 1.0,
                1.0, -1.0, 1.0,
                -1.0, -1.0, 1.0,
                -1.0, 1.0, 1.0,

                // Back
                1.0, 1.0, -1.0,
                1.0, -1.0, -1.0,
                -1.0, -1.0, -1.0,
                -1.0, 1.0, -1.0,

                // Bottom
                -1.0, -1.0, -1.0,
                -1.0, -1.0, 1.0,
                1.0, -1.0, 1.0,
                1.0, -1.0, -1.0
        ).map { it.toFloat() }.toTypedArray()

        val textureCoords = arrayOf(
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0
        ).map { it.toFloat() }.toTypedArray()

        indices = arrayOf(
                // Top
                0, 1, 2,
                0, 2, 3,

                // Left
                5, 4, 6,
                6, 4, 7,

                // Right
                8, 9, 10,
                8, 10, 11,

                // Front
                13, 12, 14,
                15, 14, 12,

                // Back
                16, 17, 18,
                16, 18, 19,

                // Bottom
                21, 20, 22,
                22, 20, 23
        ).map { it.toShort() }.toTypedArray()

        val positionVBO = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, positionVBO)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(vertices), WebGLRenderingContext.STATIC_DRAW)
        val positionAttribute = gl.getAttribLocation(shader.program, "position")
        gl.vertexAttribPointer(positionAttribute, 3, WebGLRenderingContext.FLOAT, false, 3 * Float32Array.BYTES_PER_ELEMENT, 0)

        val indexVBO = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, indexVBO)
        gl.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, Uint16Array(indices), WebGLRenderingContext.STATIC_DRAW)

        val textureCoordsVBO = gl.createBuffer()
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, textureCoordsVBO)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array(textureCoords), WebGLRenderingContext.STATIC_DRAW)
        val textureCoordsAttribute = gl.getAttribLocation(shader.program, "textureCoords")
        gl.vertexAttribPointer(textureCoordsAttribute, 2, WebGLRenderingContext.FLOAT, false, 2 * Float32Array.BYTES_PER_ELEMENT, 0)

        gl.enableVertexAttribArray(positionAttribute)
        gl.enableVertexAttribArray(textureCoordsAttribute)

        texture = ModelLoading.loadTexture(gl,"texture_crate")

        shader.start()

        Matrix4f.identity(modelMatrix)
        Matrix4f.identity(viewMatrix)
        GLMatrix.mat4.lookAt(viewMatrix.floatArray, arrayOf(0.0f, 15.0f, -20.0f), arrayOf(0.0f, 0.0f, 0.0f), arrayOf(0.0f, 1.0f, 0.0f))
        Matrix4f.projectionMatrix(projectionMatrix, 70.0f, canvas.width, canvas.height, 0.1f, 1000.0f)

        GLMatrix.mat4.identity(identity)

        modelMatrixLocation = gl.getUniformLocation(shader.program, "modelMatrix") ?: throw IllegalStateException()
        val viewMatrixLocation = gl.getUniformLocation(shader.program, "viewMatrix")
        val projectionMatrixLocation = gl.getUniformLocation(shader.program, "projectionMatrix")

        gl.uniformMatrix4fv(modelMatrixLocation, false, modelMatrix.floatArray)
        gl.uniformMatrix4fv(viewMatrixLocation, false, viewMatrix.floatArray)
        gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix.floatArray)
    }

    fun clear() {
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)
    }

    fun render(x: Float, y: Float, z: Float) {

        angle = window.performance.now().toFloat() / 1000.0f / 6.0f * 2.0f * PI.toFloat()
        //GLMatrix.mat4.rotate(modelMatrix.floatArray, identity, angle, arrayOf(0.0f, 1.0f, 0.0f))
        Matrix4f.identity(modelMatrix)
        GLMatrix.mat4.translate(modelMatrix.floatArray, modelMatrix.floatArray, arrayOf(x, y, z))
        //Matrix4f.translate(modelMatrix, 0.2f, 0.0f, 0.0f)
        gl.uniformMatrix4fv(modelMatrixLocation, false, modelMatrix.floatArray)

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture)
        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.drawElements(WebGLRenderingContext.TRIANGLES, indices.size, WebGLRenderingContext.UNSIGNED_SHORT, 0)
    }
}