package ch.awesome.game.engine.rendering

import ch.awesome.game.lib.webgl2.WebGLVertexArrayObject

/**
 * Represents a renderable model
 */
class Model (val vao: WebGLVertexArrayObject, val vertexCount: Int) {}