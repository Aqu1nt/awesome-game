package ch.awesome.game.client.rendering.shader.particle

//language=GLSL
val particleVertexShaderSource = """
#version 300 es
precision mediump float;

in vec3 position;
in vec2 textureCoords;

out vec2 passTextureCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
  mat4 modelView = viewMatrix * modelMatrix;

  // First colunm.
  modelView[0][0] = 1.0;
  modelView[0][1] = 0.0;
  modelView[0][2] = 0.0;

  // Second colunm.
  modelView[1][0] = 0.0;
  modelView[1][1] = 1.0;
  modelView[1][2] = 0.0;

  // Thrid colunm.
  modelView[2][0] = 0.0;
  modelView[2][1] = 0.0;
  modelView[2][2] = 1.0;

  passTextureCoords = textureCoords;
  gl_Position = projectionMatrix * modelView * vec4(position, 1);
}
""".trimIndent()