package ch.awesome.game.client.rendering.shader.skybox

//language=GLSL
val skyboxFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec3 textureCoords;
uniform samplerCube cubeMap;
out vec4 outColor;

void main() {
    outColor = texture(cubeMap, textureCoords);
}
""".trimIndent()