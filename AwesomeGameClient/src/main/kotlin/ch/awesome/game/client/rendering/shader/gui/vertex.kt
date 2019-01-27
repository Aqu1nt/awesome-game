package ch.awesome.game.client.rendering.shader.gui

//language=GLSL
val guiVertexShaderSource = """
#version 300 es
precision mediump float;

in vec2 position;
uniform mat4 modelMatrix;
uniform vec2 texCoordsLeftTop;
uniform vec2 texCoordsRightBottom;
out vec2 textureCoords;

void main() {
    gl_Position = modelMatrix * vec4(position, 0.0, 1.0);
    textureCoords = vec2((position.x + 1.0) / 2.0, 1.0 - (position.y + 1.0) / 2.0);

    if(position.x == -1.0 && position.y == -1.0) textureCoords = vec2(texCoordsLeftTop.x, texCoordsRightBottom.y);
	else if(position.x == 1.0 && position.y == -1.0) textureCoords = texCoordsRightBottom;
	else if(position.x == -1.0 && position.y == 1.0) textureCoords = texCoordsLeftTop;
	else if(position.x == 1.0 && position.y == 1.0) textureCoords = vec2(texCoordsRightBottom.x, texCoordsLeftTop.y);
	else textureCoords = vec2((texCoordsLeftTop + texCoordsRightBottom) / 2.0);
}
""".trimIndent()