package ch.awesome.game.client.rendering.shader.skybox

//language=GLSL
val skyboxVertexShaderSource = """
#version 300 es
precision mediump float;

in vec3 position;
out vec3 textureCoords;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
    textureCoords = position;
}
""".trimIndent()