package ch.awesome.game.client.rendering.shader.model

import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.shader.ShaderProgram
import ch.awesome.game.client.rendering.shader.uniforms.UniformFloat
import ch.awesome.game.client.rendering.shader.uniforms.UniformMatrix4f
import ch.awesome.game.client.rendering.shader.uniforms.UniformVector3fArray
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class ModelShader(val gl: WebGL2RenderingContext) : ShaderProgram(gl, modelVertexShaderSource, modelFragmentShaderSource) {

    val uniformModelMatrix = UniformMatrix4f("modelMatrix").apply { uniforms.add(this) }
    val uniformViewMatrix = UniformMatrix4f("viewMatrix").apply { uniforms.add(this) }
    val uniformProjectionMatrix = UniformMatrix4f("projectionMatrix").apply { uniforms.add(this) }
    val uniformLightPos = UniformVector3fArray("lightPos", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformLightColor = UniformVector3fArray("lightColor", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformLightAttenuation =
            UniformVector3fArray("lightAttenuation", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformAmbientLight = UniformFloat("ambientLight").apply { uniforms.add(this) }
    val uniformReflectivity = UniformFloat("reflectivity").apply { uniforms.add(this) }
    val uniformShineDamper = UniformFloat("shineDamper").apply { uniforms.add(this) }
}