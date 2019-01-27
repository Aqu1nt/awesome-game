package ch.awesome.game.client.rendering.shader.postprocessing

//language=GLSL
val contrastFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 textureCoords;
uniform sampler2D targetTexture;
out vec4 outColor;

const float contrast = 0.3;

void main() {
    outColor = texture(targetTexture, textureCoords);
//    outColor.rgb = vec3(1.0, 1.0, 1.0) - outColor.rgb;
    outColor.rgb = (outColor.rgb - 0.5) * (1.0 + contrast) + 0.5;
}
""".trimIndent()