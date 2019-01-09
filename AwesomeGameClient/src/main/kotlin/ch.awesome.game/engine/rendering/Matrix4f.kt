package ch.awesome.game.engine.rendering

import ch.awesome.game.utils.IVector3f
import ch.awesome.game.utils.Vector3f
import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
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
            matrix.floatArray[12] += matrix.floatArray[0] * x + matrix.floatArray[4] * y + matrix.floatArray[8] * z
            matrix.floatArray[13] += matrix.floatArray[1] * x + matrix.floatArray[5] * y + matrix.floatArray[9] * z
            matrix.floatArray[14] += matrix.floatArray[2] * x + matrix.floatArray[6] * y + matrix.floatArray[10] * z
            matrix.floatArray[15] += matrix.floatArray[3] * x + matrix.floatArray[7] * y + matrix.floatArray[11] * z

            return  matrix
        }

        fun scale(matrix: Matrix4f, x: Float, y: Float, z: Float): Matrix4f {
            matrix.floatArray[0] *= x
            matrix.floatArray[1] *= x
            matrix.floatArray[2] *= x
            matrix.floatArray[3] *= x

            matrix.floatArray[4] *= y
            matrix.floatArray[5] *= y
            matrix.floatArray[6] *= y
            matrix.floatArray[7] *= y

            matrix.floatArray[8] *= z
            matrix.floatArray[9] *= z
            matrix.floatArray[10] *= z
            matrix.floatArray[11] *= z

            return matrix
        }

        fun rotate(matrix: Matrix4f, angle: Float, axis: IVector3f): Matrix4f {
            val sin = sin(toRadians(angle))
            val cos = cos(toRadians(angle))
            val oneMinusCos = 1.0f - cos
            val xy = axis.x * axis.y
            val xz = axis.x * axis.z
            val yz = axis.y * axis.z
            val xSin = axis.x * sin
            val ySin = axis.y * sin
            val zSin = axis.z * sin

            val raw0 = axis.x * axis.x * oneMinusCos + cos
            val raw1 = xy * oneMinusCos + zSin
            val raw2 = xz * oneMinusCos - ySin
            val raw4 = xy * oneMinusCos - zSin
            val raw5 = axis.y * axis.y * oneMinusCos + cos
            val raw6 = yz * oneMinusCos + xSin
            val raw8 = xz * oneMinusCos + ySin
            val raw9 = yz * oneMinusCos - xSin
            val raw10 = axis.z * axis.z * oneMinusCos + cos

            val final0 = matrix.floatArray[0] * raw0 + matrix.floatArray[4] * raw1 + matrix.floatArray[8] * raw2
            val final1 = matrix.floatArray[1] * raw0 + matrix.floatArray[5] * raw1 + matrix.floatArray[9] * raw2
            val final2 = matrix.floatArray[2] * raw0 + matrix.floatArray[6] * raw1 + matrix.floatArray[10] * raw2
            val final3 = matrix.floatArray[3] * raw0 + matrix.floatArray[7] * raw1 + matrix.floatArray[11] * raw2
            val final4 = matrix.floatArray[0] * raw4 + matrix.floatArray[4] * raw5 + matrix.floatArray[8] * raw6
            val final5 = matrix.floatArray[1] * raw4 + matrix.floatArray[5] * raw5 + matrix.floatArray[9] * raw6
            val final6 = matrix.floatArray[2] * raw4 + matrix.floatArray[6] * raw5 + matrix.floatArray[10] * raw6
            val final7 = matrix.floatArray[3] * raw4 + matrix.floatArray[7] * raw5 + matrix.floatArray[11] * raw6

            matrix.floatArray[8] = matrix.floatArray[0] * raw8 + matrix.floatArray[4] * raw9 + matrix.floatArray[8] * raw10
            matrix.floatArray[9] = matrix.floatArray[1] * raw8 + matrix.floatArray[5] * raw9 + matrix.floatArray[9] * raw10
            matrix.floatArray[10] = matrix.floatArray[2] * raw8 + matrix.floatArray[6] * raw9 + matrix.floatArray[10] * raw10
            matrix.floatArray[11] = matrix.floatArray[3] * raw8 + matrix.floatArray[7] * raw9 + matrix.floatArray[11] * raw10
            matrix.floatArray[0] = final0
            matrix.floatArray[1] = final1
            matrix.floatArray[2] = final2
            matrix.floatArray[3] = final3
            matrix.floatArray[4] = final4
            matrix.floatArray[5] = final5
            matrix.floatArray[6] = final6
            matrix.floatArray[7] = final7

            return matrix
        }

        fun modelMatrix(matrix: Matrix4f, position: IVector3f, rotation: IVector3f, scale: IVector3f): Matrix4f {
            Matrix4f.identity(matrix)

            translate(matrix, position.x, position.y, position.z)
            rotate(matrix, rotation.x, Vector3f(1.0f, 0.0f, 0.0f))
            rotate(matrix, rotation.y, Vector3f(0.0f, 1.0f, 0.0f))
            rotate(matrix, rotation.z, Vector3f(0.0f, 0.0f, 1.0f))
            scale(matrix, scale.x, scale.y, scale.z)

            return matrix
        }

        fun viewMatrix(matrix: Matrix4f, x: Float, y: Float, z: Float, pitch: Float, yaw: Float, roll: Float): Matrix4f {
            identity(matrix)

            rotate(matrix, pitch, Vector3f(1.0f, 0.0f, 0.0f))
            rotate(matrix, yaw, Vector3f(0.0f, 1.0f, 0.0f))
            rotate(matrix, roll, Vector3f(0.0f, 0.0f, 1.0f))
            translate(matrix, -x, -y, -z)

            return  matrix
        }

        fun projectionMatrix(matrix: Matrix4f, fov: Float, cWidth: Int, cHeight: Int, nearPlane: Float, farPlane: Float): Matrix4f {
            identity(matrix)

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