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
    }

    fun identity(): Matrix4f {
        m00 = 1.0f
        m01 = 0.0f
        m02 = 0.0f
        m03 = 0.0f

        m10 = 0.0f
        m11 = 1.0f
        m12 = 0.0f
        m13 = 0.0f

        m20 = 0.0f
        m21 = 0.0f
        m22 = 1.0f
        m23 = 0.0f

        m30 = 0.0f
        m31 = 0.0f
        m32 = 0.0f
        m33 = 1.0f
        
        return this
    }

    fun translate(vec: IVector3f): Matrix4f {
        return translate(vec.x, vec.y, vec.z)
    }

    fun translate(x: Float, y: Float, z: Float): Matrix4f {
        m30 += m00 * x + m10 * y + m20 * z
        m31 += m01 * x + m11 * y + m21 * z
        m32 += m02 * x + m12 * y + m22 * z
        m33 += m03 * x + m13 * y + m23 * z

        return this
    }

    fun scale(vec: IVector3f): Matrix4f {
        return scale(vec.x, vec.y, vec.z)
    }

    fun scale(x: Float, y: Float, z: Float): Matrix4f {
        m00 *= x
        m01 *= x
        m02 *= x
        m03 *= x

        m10 *= y
        m11 *= y
        m12 *= y
        m13 *= y

        m20 *= z
        m21 *= z
        m22 *= z
        m23 *= z

        return this
    }

    fun rotate(vec: IVector3f): Matrix4f {
        rotate(vec.x, X_AXIS)
        rotate(vec.y, Y_AXIS)
        rotate(vec.z, Z_AXIS)
        return this
    }

    fun rotate(angle: Float, axis: IVector3f): Matrix4f {
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

        val final0 = m00 * raw0 + m10 * raw1 + m20 * raw2
        val final1 = m01 * raw0 + m11 * raw1 + m21 * raw2
        val final2 = m02 * raw0 + m12 * raw1 + m22 * raw2
        val final3 = m03 * raw0 + m13 * raw1 + m23 * raw2
        val final4 = m00 * raw4 + m10 * raw5 + m20 * raw6
        val final5 = m01 * raw4 + m11 * raw5 + m21 * raw6
        val final6 = m02 * raw4 + m12 * raw5 + m22 * raw6
        val final7 = m03 * raw4 + m13 * raw5 + m23 * raw6

        m20 = m00 * raw8 + m10 * raw9 + m20 * raw10
        m21 = m01 * raw8 + m11 * raw9 + m21 * raw10
        m22 = m02 * raw8 + m12 * raw9 + m22 * raw10
        m23 = m03 * raw8 + m13 * raw9 + m23 * raw10
        m00 = final0
        m01 = final1
        m02 = final2
        m03 = final3
        m10 = final4
        m11 = final5
        m12 = final6
        m13 = final7

        return this
    }

    fun rotate(direction: Vector3f): Matrix4f {
        val xAxis = Vector3f(0.0f, 1.0f, 0.0f).cross(direction)
        xAxis.normalize()

        val yAxis = direction.cross(xAxis)
        yAxis.normalize()

        m00 = xAxis.x
        m10 = yAxis.x
        m20 = direction.x

        m01 = xAxis.y
        m11 = yAxis.y
        m21 = direction.y

        m02 = xAxis.z
        m12 = yAxis.z
        m22 = direction.z

        return this
    }

    fun modelMatrix(position: IVector3f, rotation: IVector3f, scale: IVector3f): Matrix4f {
        identity()

        translate(position.x, position.y, position.z)
        rotate(rotation.x, Vector3f(1.0f, 0.0f, 0.0f))
        rotate(rotation.y, Vector3f(0.0f, 1.0f, 0.0f))
        rotate(rotation.z, Vector3f(0.0f, 0.0f, 1.0f))
        scale(scale.x, scale.y, scale.z)

        return this
    }

    fun viewMatrix(x: Float, y: Float, z: Float, pitch: Float, yaw: Float, roll: Float): Matrix4f {
        identity()

        rotate(pitch, Vector3f(1.0f, 0.0f, 0.0f))
        rotate(yaw, Vector3f(0.0f, 1.0f, 0.0f))
        rotate(roll, Vector3f(0.0f, 0.0f, 1.0f))
        translate(-x, -y, -z)

        return this
    }

    fun viewMatrix(x: Float, y: Float, z: Float, dir: Vector3f): Matrix4f {
        identity()

        rotate(dir)
        translate(-x, -y, -z)

        return this
    }

    fun projectionMatrix(fov: Float, cWidth: Int, cHeight: Int, nearPlane: Float, farPlane: Float): Matrix4f {
        identity()

        val aspectRatio = cWidth.toFloat() / cHeight.toFloat()
        val yScale = (1.0f / tan(toRadians(fov / 2.0f)) * aspectRatio)
        val xScale = (yScale / aspectRatio)
        val frustumLength = farPlane - nearPlane

        m00 = xScale
        m01 = 0.0f
        m02 = 0.0f
        m03 = 0.0f
        m10 = 0.0f
        m11 = yScale
        m12 = 0.0f
        m13 = 0.0f
        m20 = 0.0f
        m21 = 0.0f
        m22 = -((farPlane + nearPlane) / frustumLength)
        m23 = -1.0f
        m30 = 0.0f
        m31 = 0.0f
        m32 = -((2.0f * nearPlane * farPlane) / frustumLength)
        m33 = 0.0f

        return this
    }

    fun rotateVect(vec: IVector3f): Matrix4f {
        val vx = vec.x
        val vy = vec.y
        val vz = vec.z

        vec.x = vx * m00 + vy * m01 + vz * m02
        vec.y = vx * m10 + vy * m11 + vz * m12
        vec.z = vx * m20 + vy * m21 + vz * m22

        return this
    }

    fun translateVect(vec: IVector3f): Matrix4f {
        vec.x += m03
        vec.y += m13
        vec.z += m23

        return this
    }

    fun toScaleVector(vector: IVector3f = Vector3f()): IVector3f {
        val scaleX = sqrt((m00 * m00 + m10 * m10 + m20 * m20).toDouble()).toFloat()
        val scaleY = sqrt((m01 * m01 + m11 * m11 + m21 * m21).toDouble()).toFloat()
        val scaleZ = sqrt((m02 * m02 + m12 * m12 + m22 * m22).toDouble()).toFloat()
        vector.x = scaleX
        vector.y = scaleY
        vector.z = scaleZ
        return vector
    }

    fun toTranslationVector(vector: IVector3f = Vector3f()): IVector3f {
        vector.x = m03
        vector.y = m13
        vector.z = m23
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