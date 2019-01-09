package ch.awesome.game.client.rendering.shader.model

import ch.awesome.game.client.rendering.GameRenderer

//language=GLSL
val modelVertexShaderSource = """
#version 300 es
precision mediump float;

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextureCoords;
out vec3 worldNormal;
out vec3 toLightVector[${GameRenderer.maxLights}];
out vec3 vecToCam;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPos[${GameRenderer.maxLights}];

void main()
{
    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    passTextureCoords = textureCoords;

    worldNormal = (modelMatrix * vec4(normal, 0.0)).xyz;

    for(int i = 0; i < ${GameRenderer.maxLights}; i++) {
        toLightVector[i] = lightPos[i] - worldPosition.xyz;
    }

    vecToCam = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}
""".trimIndent()