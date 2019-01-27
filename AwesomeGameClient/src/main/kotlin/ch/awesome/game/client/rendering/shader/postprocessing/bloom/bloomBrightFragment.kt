package ch.awesome.game.client.rendering.shader.postprocessing.bloom

//language=GLSL
val bloomBrightFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 textureCoords;
uniform sampler2D sceneTexture;
out vec4 outColor;

void main() {
    vec4 color = texture(sceneTexture, textureCoords);
    float brightness = (color.r * 0.2126) + (color.g * 0.7152) + (color.b * 0.0722);

    if(brightness >= 0.9) {
        outColor = color;
    } else {
        outColor = vec4(0.0);
    }
}
""".trimIndent()