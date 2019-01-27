package ch.awesome.game.client.rendering.shader.postprocessing.bloom

//language=GLSL
val bloomVertexShaderSource = """
#version 300 es
precision mediump float;

in vec2 position;
out vec2 textureCoords;

void main() {
    gl_Position = vec4(position, 0.0, 1.0);
    textureCoords = position * 0.5 + 0.5;
}
""".trimIndent()