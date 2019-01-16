package ch.awesome.game.client.rendering.shader.gui

//language=GLSL
val guiFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 textureCoords;
uniform sampler2D guiTexture;
out vec4 outColor;

void main() {
    outColor = texture(guiTexture, textureCoords);
}
""".trimIndent()