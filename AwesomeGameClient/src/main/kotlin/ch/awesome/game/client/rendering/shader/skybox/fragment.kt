package ch.awesome.game.client.rendering.shader.skybox

//language=GLSL
val skyboxFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec3 textureCoords;

uniform samplerCube cubeMap;
uniform vec3 skyColor;

out vec4 outColor;

const float upperFogLimit = 100.0;
const float lowerFogLimit = -100.0;

void main() {
    outColor = texture(cubeMap, textureCoords);

    float visibility = (textureCoords.y - lowerFogLimit) / (upperFogLimit - lowerFogLimit);
    visibility = clamp(visibility, 0.0, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}
""".trimIndent()