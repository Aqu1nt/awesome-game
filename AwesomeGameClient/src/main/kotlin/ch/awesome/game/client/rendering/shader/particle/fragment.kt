package ch.awesome.game.client.rendering.shader.particle

//language=GLSL
val particleFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 passTextureCoords;

out vec4 outColor;

uniform vec4 color;
uniform sampler2D modelTexture;

void main() {
    outColor = color * texture(modelTexture, passTextureCoords);
}
""".trimIndent()