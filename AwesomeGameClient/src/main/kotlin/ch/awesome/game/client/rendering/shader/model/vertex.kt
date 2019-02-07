package ch.awesome.game.client.rendering.shader.model

import ch.awesome.game.client.rendering.renderer.GameRenderer

//language=GLSL
val modelVertexShaderSource = """
#version 300 es
precision mediump float;

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextureCoords;
out vec3 worldNormal;
out vec3 vecToLight[${GameRenderer.MAX_LIGHTS}];
out vec3 vecToDirLight;
out vec3 vecToCam;
out float visibility;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPos[${GameRenderer.MAX_LIGHTS}];
uniform vec3 directionalLightPos;

const float density = 0.003;
const float gradient = 5.0;

void main() {
    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;
    passTextureCoords = textureCoords;

    worldNormal = (modelMatrix * vec4(normal, 0.0)).xyz;

    for(int i = 0; i < ${GameRenderer.MAX_LIGHTS}; i++) {
        vecToLight[i] = lightPos[i] - worldPosition.xyz;
    }

    vecToDirLight = directionalLightPos - position.xyz;
    vecToCam = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
""".trimIndent()