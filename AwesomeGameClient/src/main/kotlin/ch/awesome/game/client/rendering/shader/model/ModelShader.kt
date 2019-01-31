package ch.awesome.game.client.rendering.shader.model

import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.rendering.shader.*
import ch.awesome.game.client.rendering.shader.uniforms.*
import ch.awesome.game.client.lib.WebGL2RenderingContext

class ModelShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, modelVertexShaderSource, modelFragmentShaderSource,
        arrayOf("position", "textureCoords", "normal")) {

    val uniformModelMatrix = UniformMatrix4f("modelMatrix").apply { uniforms.add(this) }
    val uniformViewMatrix = UniformMatrix4f("viewMatrix").apply { uniforms.add(this) }
    val uniformProjectionMatrix = UniformMatrix4f("projectionMatrix").apply { uniforms.add(this) }
    val uniformModelTexture = UniformInt("modelTexture").apply { uniforms.add(this) }
    val uniformLightMap = UniformInt("lightMap").apply { uniforms.add(this) }
    val uniformLightPos = UniformVector3fArray("lightPos", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformLightColor = UniformVector3fArray("lightColor", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformLightAttenuation =
            UniformVector3fArray("lightAttenuation", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformAmbientLight = UniformFloat("ambientLight").apply { uniforms.add(this) }
    val uniformReflectivity = UniformFloat("reflectivity").apply { uniforms.add(this) }
    val uniformShineDamper = UniformFloat("shineDamper").apply { uniforms.add(this) }
    val uniformDirectionalLightPos = UniformVector3f("directionalLightPos").apply { uniforms.add(this) }
    val uniformDirectionalLightColor = UniformVector3f("directionalLightColor").apply { uniforms.add(this) }
    val uniformUseLightMap = UniformBoolean("useLightMap").apply { uniforms.add(this) }
    val uniformSkyColor = UniformVector3f("skyColor").apply { uniforms.add(this) }
}