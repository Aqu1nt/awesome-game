package ch.awesome.game.lib.glmatrix

import org.khronos.webgl.Float32Array
import org.w3c.dom.svg.SVGAnimatedPreserveAspectRatio

@JsName("glMatrix")
external class GLMatrix {
    companion object {
        val mat4: GLMatrix4
    }
}

abstract external class GLMatrix4 {
    fun identity(matrix: Float32Array)
    fun lookAt(matrix: Float32Array, position: Array<Float>, target: Array<Float>, eye: Array<Float>)
    fun perspective(matrix: Float32Array, fov: Float, aspectRatio: Float, nearPlane: Float, farPlane: Float)
    fun rotate(matrix: Float32Array, with: Float32Array, angle: Float, axis: Array<Float>)
}