package ch.awesome.game.client.rendering.shader.postprocessing.bloom

//language=GLSL
val bloomCombineFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 textureCoords;
uniform sampler2D sceneTexture;
uniform sampler2D brightTexture;
out vec4 outColor;

void main() {
    vec4 sceneColor = texture(sceneTexture, textureCoords);
    vec4 brightColor = texture(brightTexture, textureCoords);

    outColor = sceneColor + brightColor;
}
""".trimIndent()