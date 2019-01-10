package ch.awesome.game.common.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class Matrix4f {

    var floatArray = Array(16) { 0.0f }
        private set

    var m00: Float get() { return floatArray[0] }  set(value) { floatArray[0] = value }
    var m01: Float get() { return floatArray[1] }  set(value) { floatArray[1] = value }
    var m02: Float get() { return floatArray[2] }  set(value) { floatArray[2] = value }
    var m03: Float get() { return floatArray[3] }  set(value) { floatArray[3] = value }
    var m10: Float get() { return floatArray[4] }  set(value) { floatArray[4] = value }
    var m11: Float get() { return floatArray[5] }  set(value) { floatArray[5] = value }
    var m12: Float get() { return floatArray[6] }  set(value) { floatArray[6] = value }
    var m13: Float get() { return floatArray[7] }  set(value) { floatArray[7] = value }
    var m20: Float get() { return floatArray[8] }  set(value) { floatArray[8] = value }
    var m21: Float get() { return floatArray[9] }  set(value) { floatArray[9] = value }
    var m22: Float get() { return floatArray[10] }  set(value) { floatArray[10] = value }
    var m23: Float get() { return floatArray[11] }  set(value) { floatArray[11] = value }
    var m30: Float get() { return floatArray[12] }  set(value) { floatArray[12] = value }
    var m31: Float get() { return floatArray[13] }  set(value) { floatArray[13] = value }
    var m32: Float get() { return floatArray[14] }  set(value) { floatArray[14] = value }
    var m33: Float get() { return floatArray[15] }  set(value) { floatArray[15] = value }

    companion object {

        val X_AXIS = Vector3f(1f, 0f, 0f)
        val Y_AXIS = Vector3f(0f, 1f, 0f)
        val Z_AXIS = Vector3f(0f, 0f, 1f)

        fun identity(matrix: Matrix4f = Matrix4f()): Matrix4f {
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

        fun translate(matrix: Matrix4f, vec: IVector3f): Matrix4f {
            return translate(matrix, vec.x, vec.y, vec.z)
        }

        fun translate(matrix: Matrix4f, x: Float, y: Float, z: Float): Matrix4f {
            matrix.floatArray[12] += matrix.floatArray[0] * x + matrix.floatArray[4] * y + matrix.floatArray[8] * z
            matrix.floatArray[13] += matrix.floatArray[1] * x + matrix.floatArray[5] * y + matrix.floatArray[9] * z
            matrix.floatArray[14] += matrix.floatArray[2] * x + matrix.floatArray[6] * y + matrix.floatArray[10] * z
            matrix.floatArray[15] += matrix.floatArray[3] * x + matrix.floatArray[7] * y + matrix.floatArray[11] * z

            return matrix
        }

        fun scale(matrix: Matrix4f, vec: IVector3f): Matrix4f {
            return scale(matrix, vec.x, vec.y, vec.z)
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

        fun rotate(matrix: Matrix4f, vec: IVector3f): Matrix4f {
            rotate(matrix, vec.x, X_AXIS)
            rotate(matrix, vec.y, Y_AXIS)
            rotate(matrix, vec.z, Z_AXIS)
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
            identity(matrix)

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

            return matrix
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

            return matrix
        }

        fun rotateVect(matrix: Matrix4f, vec: IVector3f): Matrix4f {
            val vx = vec.x
            val vy = vec.y
            val vz = vec.z

            vec.x = vx * matrix.floatArray[0] + vy * matrix.floatArray[1] + vz * matrix.floatArray[2]
            vec.y = vx * matrix.floatArray[4] + vy * matrix.floatArray[5] + vz * matrix.floatArray[6]
            vec.z = vx * matrix.floatArray[8] + vy * matrix.floatArray[9] + vz * matrix.floatArray[10]

            return matrix
        }

        fun translateVect(matrix: Matrix4f, vec: IVector3f): Matrix4f {
            vec.x += matrix.floatArray[3]
            vec.y += matrix.floatArray[7]
            vec.z += matrix.floatArray[11]

            return matrix
        }
    }


    fun toScaleVector(vector: IVector3f = Vector3f()): IVector3f {
        val scaleX = sqrt((floatArray[0] * floatArray[0] + floatArray[4] * floatArray[4] + floatArray[8] * floatArray[8]).toDouble()).toFloat()
        val scaleY = sqrt((floatArray[1] * floatArray[1] + floatArray[5] * floatArray[5] + floatArray[9] * floatArray[9]).toDouble()).toFloat()
        val scaleZ = sqrt((floatArray[2] * floatArray[2] + floatArray[6] * floatArray[6] + floatArray[10] * floatArray[10]).toDouble()).toFloat()
        vector.x = scaleX
        vector.y = scaleY
        vector.z = scaleZ
        return vector
    }

    fun toTranslationVector(vector: IVector3f = Vector3f()): IVector3f {
        vector.x = floatArray[3]
        vector.y = floatArray[7]
        vector.z = floatArray[11]
        return vector
    }

    fun setRotationQuaternion(quat: Quaternion) {
        quat.toRotationMatrix(this)
    }

    fun set(other: Matrix4f): Matrix4f {
        other.floatArray.forEachIndexed { index, value -> floatArray[index] = value }
        return this
    }

    fun setScale(vec: IVector3f) {
        setScale(vec.x, vec.y, vec.z)
    }

    fun setScale(x: Float, y: Float, z: Float) {
        val vect1 = Vector3f()

        vect1.set(m00, m10, m20)
        vect1.normalizeLocal().multLocal(x)
        m00 = vect1.x
        m10 = vect1.y
        m20 = vect1.z

        vect1.set(m01, m11, m21)
        vect1.normalizeLocal().multLocal(y)
        m01 = vect1.x
        m11 = vect1.y
        m21 = vect1.z

        vect1.set(m02, m12, m22)
        vect1.normalizeLocal().multLocal(z)
        m02 = vect1.x
        m12 = vect1.y
        m22 = vect1.z
    }

    fun copy(): Matrix4f {
        return Matrix4f().also {
            it.floatArray = floatArray.copyOf()
        }
    }
}