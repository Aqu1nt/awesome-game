package ch.awesome.game.client.rendering.shader.postprocessing.gaussianblur

//language=GLSL
val verticalBlurVertexShaderSource = """
#version 300 es
precision mediump float;

in vec2 position;
uniform float targetHeight;
out vec2 textureCoords[11];

void main() {
    gl_Position = vec4(position, 0.0, 1.0);
    vec2 centerTextureCoords = position * 0.5 + 0.5;
    float pixelSize = 1.0 / targetHeight;

    for(int i = -5; i <= 5; i++) {
        textureCoords[i + 5] = centerTextureCoords + vec2(0.0, pixelSize * float(i));
    }
}
""".trimIndent()