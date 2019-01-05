package ch.awesome.game.engine.rendering

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import kotlin.math.PI
import kotlin.math.tan

class Matrix4f {

    var floatArray = Float32Array(16)

    companion object {

        fun identity(matrix: Matrix4f): Matrix4f {
            matrix.floatArray[0] = 1.0f
            matrix.floatArray[1] = 0.0f
            matrix.floatArray[2] = 0.0f
            matrix.floatArray[3] = 0.0f

            matrix.floatArray[4] = 0.0f
            matrix.floatArray[5] = 1.0f
            matrix.floatArray[6] = 0.0f
            matrix.floatArray[7] = 0.0f

            matrix.floatArray[8] = 0.0f
            matrix.floatArray[9] = 0.0f
            matrix.floatArray[10] = 1.0f
            matrix.floatArray[11] = 0.0f

            matrix.floatArray[12] = 0.0f
            matrix.floatArray[13] = 0.0f
            matrix.floatArray[14] = 0.0f
            matrix.floatArray[15] = 1.0f

            return matrix
        }

        fun translate(matrix: Matrix4f, x: Float, y: Float, z: Float): Matrix4f {
            matrix.floatArray[3] += matrix.floatArray[0] * x + matrix.floatArray[1] * y + matrix.floatArray[2] * z
            matrix.floatArray[3] += matrix.floatArray[4] * x + matrix.floatArray[5] * y + matrix.floatArray[6] * z
            matrix.floatArray[11] += matrix.floatArray[8] * x + matrix.floatArray[9] * y + matrix.floatArray[10] * z
            matrix.floatArray[15] += matrix.floatArray[12] * x + matrix.floatArray[13] * y + matrix.floatArray[14] * z

            return  matrix
        }

        fun viewMatrix(matrix: Matrix4f, position: Array<Float>, rotation: Array<Float>): Matrix4f {
            return  matrix
        }

        fun projectionMatrix(matrix: Matrix4f, fov: Float, cWidth: Int, cHeight: Int, nearPlane: Float, farPlane: Float): Matrix4f {
            val aspectRatio = cWidth.toFloat() / cHeight.toFloat()
            val yScale = (1.0f / tan(toRadians(fov / 2.0f)) * aspectRatio)
            val xScale = (yScale / aspectRatio)
            val frustumLength = farPlane - nearPlane

            matrix.floatArray[0] = xScale
            matrix.floatArray[1] = 0.0f
            matrix.floatArray[2] = 0.0f
            matrix.floatArray[3] = 0.0f
            matrix.floatArray[4] = 0.0f
            matrix.floatArray[5] = yScale
            matrix.floatArray[6] = 0.0f
            matrix.floatArray[7] = 0.0f
            matrix.floatArray[8] = 0.0f
            matrix.floatArray[9] = 0.0f
            matrix.floatArray[10] = -((farPlane + nearPlane) / frustumLength)
            matrix.floatArray[11] = -1.0f
            matrix.floatArray[12] = 0.0f
            matrix.floatArray[13] = 0.0f
            matrix.floatArray[14] = -((2.0f * nearPlane * farPlane) / frustumLength)
            matrix.floatArray[15] = 0.0f

            return  matrix
        }

        fun toRadians(degrees: Float): Float {
            return  degrees / 180.0f * PI.toFloat()
        }
    }
}