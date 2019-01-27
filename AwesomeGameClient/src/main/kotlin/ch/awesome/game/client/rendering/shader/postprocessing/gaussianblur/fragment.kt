package ch.awesome.game.client.rendering.shader.postprocessing.gaussianblur

//language=GLSL
val blurFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 textureCoords[11];
uniform sampler2D targetTexture;
out vec4 outColor;

void main() {
    outColor = vec4(0.0);

    outColor += texture(targetTexture, textureCoords[0]) * 0.0093;
    outColor += texture(targetTexture, textureCoords[1]) * 0.028002;
    outColor += texture(targetTexture, textureCoords[2]) * 0.065984;
    outColor += texture(targetTexture, textureCoords[3]) * 0.121703;
    outColor += texture(targetTexture, textureCoords[4]) * 0.175713;
    outColor += texture(targetTexture, textureCoords[5]) * 0.198596;
    outColor += texture(targetTexture, textureCoords[6]) * 0.175713;
    outColor += texture(targetTexture, textureCoords[7]) * 0.121703;
    outColor += texture(targetTexture, textureCoords[8]) * 0.065984;
    outColor += texture(targetTexture, textureCoords[9]) * 0.028002;
    outColor += texture(targetTexture, textureCoords[10]) * 0.0093;
}
""".trimIndent()