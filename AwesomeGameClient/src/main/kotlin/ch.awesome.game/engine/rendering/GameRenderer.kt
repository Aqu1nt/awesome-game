package ch.awesome.game.engine.rendering

import ch.awesome.game.lib.glmatrix.GLMatrix
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.lib.webgl2.WebGLVertexArrayObject
import org.khronos.webgl.*
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.window
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val vertexShaderSource = """
#version 300 es
precision mediump float;

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextureCoords;
out vec3 worldNormal;
out vec3 toLightVector;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPos;

void main()
{
    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    passTextureCoords = textureCoords;

    worldNormal = (modelMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPos - worldPosition.xyz;
}
""".trimIndent()

val fragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 passTextureCoords;
in vec3 worldNormal;
in vec3 toLightVector;

out vec4 outColor;

uniform sampler2D modelTexture;
uniform vec3 lightColor;

void main()
{
    vec3 unitNormal = normalize(worldNormal);
    vec3 unitToLightVector = normalize(toLightVector);

    float dot = dot(unitNormal, unitToLightVector);
    float brightness = max(dot, 0.0);
    vec3 diffuse = brightness * lightColor;

    outColor = vec4(diffuse, 1.0) * texture(modelTexture, passTextureCoords);
}
""".trimIndent()

class GameRenderer (canvas: HTMLCanvasElement) {

    val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

    var modelMatrix = Matrix4f()
    var viewMatrix = Matrix4f()
    var projectionMatrix = Matrix4f()

    lateinit var modelMatrixLocation: WebGLUniformLocation
    lateinit var viewMatrixLocation: WebGLUniformLocation
    lateinit var lightPosLocation: WebGLUniformLocation
    lateinit var lightColorLocation: WebGLUniformLocation

    lateinit var light: Light

    val shader = ShaderProgram(gl, vertexShaderSource, fragmentShaderSource)

    init {
        canvas.width = window.innerWidth
        canvas.height = window.innerHeight

        gl.viewport(0, 0, canvas.width, canvas.height)
        gl.clearColor(0.2f, 0.5f, 0.8f, 1.0f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)
        gl.enable(WebGLRenderingContext.CULL_FACE)
        gl.cullFace(WebGLRenderingContext.BACK)

        shader.start()

        Matrix4f.identity(modelMatrix)
        Matrix4f.identity(viewMatrix)
        Matrix4f.projectionMatrix(projectionMatrix, 70.0f, canvas.width, canvas.height, 0.1f, 1000.0f)

        modelMatrixLocation = gl.getUniformLocation(shader.program, "modelMatrix") ?: throw IllegalStateException()
        viewMatrixLocation = gl.getUniformLocation(shader.program, "viewMatrix") ?: throw IllegalStateException()
        lightPosLocation = gl.getUniformLocation(shader.program, "lightPos") ?: throw IllegalStateException()
        lightColorLocation = gl.getUniformLocation(shader.program, "lightColor") ?: throw IllegalStateException()
        val projectionMatrixLocation = gl.getUniformLocation(shader.program, "projectionMatrix")

        gl.uniformMatrix4fv(modelMatrixLocation, false, modelMatrix.floatArray)
        gl.uniformMatrix4fv(viewMatrixLocation, false, viewMatrix.floatArray)
        gl.uniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix.floatArray)

        shader.stop()
    }

    fun prepare(light: Light) {
        shader.start()
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT shl WebGLRenderingContext.DEPTH_BUFFER_BIT)

        this.light = light
    }

    fun render(model: TexturedModel, x: Float, y: Float, z: Float) {
        gl.bindVertexArray(model.rawModel.vao)
        gl.enableVertexAttribArray(0)
        gl.enableVertexAttribArray(1)
        gl.enableVertexAttribArray(2)

        Matrix4f.identity(modelMatrix)
        Matrix4f.translate(modelMatrix, x, y, z)
        gl.uniformMatrix4fv(modelMatrixLocation, false, modelMatrix.floatArray)

        gl.uniform3f(lightPosLocation, light.position.x, light.position.y, light.position.z)
        gl.uniform3f(lightColorLocation, light.color.x, light.color.y, light.color.z)

        val angle = window.performance.now().toFloat() / 50.0f
        val dirX = sin(Matrix4f.toRadians(angle))
        val dirZ = cos(Matrix4f.toRadians(angle))
        val posX = dirX * 30.0f
        val posZ = dirZ * 30.0f

        Matrix4f.identity(viewMatrix)
        GLMatrix.mat4.lookAt(viewMatrix.floatArray, arrayOf(posX, 30.0f, posZ), arrayOf(0.0f, 0.0f, 0.0f), arrayOf(0.0f, 1.0f, 0.0f))
        //Matrix4f.translate(viewMatrix, 0.0f, 0.0f, 0.0f)
        //Matrix4f.rotate(viewMatrix, window.performance.now().toFloat() / 100.0f, arrayOf(0.0f, 1.0f, 0.0f))
        gl.uniformMatrix4fv(viewMatrixLocation, false, viewMatrix.floatArray)

        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, model.texture)
        gl.activeTexture(WebGLRenderingContext.TEXTURE0)
        gl.drawElements(WebGLRenderingContext.TRIANGLES, model.rawModel.vertexCount, WebGLRenderingContext.UNSIGNED_SHORT, 0)

        gl.disableVertexAttribArray(0)
        gl.disableVertexAttribArray(1)
        gl.disableVertexAttribArray(2)
        gl.bindVertexArray(null)
    }

    fun end() {
        shader.stop()
    }
}