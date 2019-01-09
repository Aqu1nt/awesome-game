package ch.awesome.game.engine.rendering.shader.model

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.engine.rendering.shader.*
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext

class ModelShader(val gl: WebGL2RenderingContext): ShaderProgram(gl, modelVertexShaderSource, modelFragmentShaderSource) {

    val uniformModelMatrix= UniformMatrix4f("modelMatrix").apply { uniforms.add(this) }
    val uniformViewMatrix= UniformMatrix4f("viewMatrix").apply { uniforms.add(this) }
    val uniformProjectionMatrix= UniformMatrix4f("projectionMatrix").apply { uniforms.add(this) }
    val uniformLightPos= UniformVector3fArray("lightPos", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformLightColor= UniformVector3fArray("lightColor", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformLightAttenuation=
            UniformVector3fArray("lightAttenuation", GameRenderer.maxLights).apply { uniforms.add(this) }
    val uniformReflectivity = UniformFloat("reflectivity").apply { uniforms.add(this) }
    val uniformShineDamper = UniformFloat("shineDamper").apply { uniforms.add(this) }
}