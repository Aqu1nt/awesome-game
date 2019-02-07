package ch.awesome.game.common.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class Matrix4f {

    var floatArray = Array(16) { 0.0f }
        private set

    var m00: Float
        get() {
            return floatArray[0]
        }
        set(value) {
            floatArray[0] = value
        }
    var m01: Float
        get() {
            return floatArray[1]
        }
        set(value) {
            floatArray[1] = value
        }
    var m02: Float
        get() {
            return floatArray[2]
        }
        set(value) {
            floatArray[2] = value
        }
    var m03: Float
        get() {
            return floatArray[3]
        }
        set(value) {
            floatArray[3] = value
        }
    var m10: Float
        get() {
            return floatArray[4]
        }
        set(value) {
            floatArray[4] = value
        }
    var m11: Float
        get() {
            return floatArray[5]
        }
        set(value) {
            floatArray[5] = value
        }
    var m12: Float
        get() {
            return floatArray[6]
        }
        set(value) {
            floatArray[6] = value
        }
    var m13: Float
        get() {
            return floatArray[7]
        }
        set(value) {
            floatArray[7] = value
        }
    var m20: Float
        get() {
            return floatArray[8]
        }
        set(value) {
            floatArray[8] = value
        }
    var m21: Float
        get() {
            return floatArray[9]
        }
        set(value) {
            floatArray[9] = value
        }
    var m22: Float
        get() {
            return floatArray[10]
        }
        set(value) {
            floatArray[10] = value
        }
    var m23: Float
        get() {
            return floatArray[11]
        }
        set(value) {
            floatArray[11] = value
        }
    var m30: Float
        get() {
            return floatArray[12]
        }
        set(value) {
            floatArray[12] = value
        }
    var m31: Float
        get() {
            return floatArray[13]
        }
        set(value) {
            floatArray[13] = value
        }
    var m32: Float
        get() {
            return floatArray[14]
        }
        set(value) {
            floatArray[14] = value
        }
    var m33: Float
        get() {
            return floatArray[15]
        }
        set(value) {
            floatArray[15] = value
        }

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
        return translate(x, y, z, this)
    }

    fun translate(x: Float, y: Float, z: Float, destination: Matrix4f): Matrix4f {
        destination.m30 += m00 * x + m10 * y + m20 * z
        destination.m31 += m01 * x + m11 * y + m21 * z
        destination.m32 += m02 * x + m12 * y + m22 * z
        destination.m33 += m03 * x + m13 * y + m23 * z

        return destination
    }

    fun scale(vec: IVector3f): Matrix4f {
        return scale(vec.x, vec.y, vec.z)
    }

    fun scale(x: Float, y: Float, z: Float): Matrix4f {
        return scale(x, y, z, this)
    }

    fun scale(x: Float, y: Float, z: Float, destination: Matrix4f): Matrix4f {
        destination.m00 = m00 * x
        destination.m01 = m01 * x
        destination.m02 = m02 * x
        destination.m03 = m03 * x

        destination.m10 = m10 * y
        destination.m11 = m11 * y
        destination.m12 = m12 * y
        destination.m13 = m13 * y

        destination.m20 = m20 * z
        destination.m21 = m21 * z
        destination.m22 = m22 * z
        destination.m23 = m23 * z

        return destination
    }

    fun rotate(vec: IVector3f): Matrix4f {
        rotate(vec.x, X_AXIS)
        rotate(vec.y, Y_AXIS)
        rotate(vec.z, Z_AXIS)
        return this
    }

    fun rotate(angle: Float, axis: IVector3f): Matrix4f {
        return rotate(angle, axis, this)
    }

    fun rotate(angle: Float, axis: IVector3f, destination: Matrix4f): Matrix4f {
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

        destination.m20 = m00 * raw8 + m10 * raw9 + m20 * raw10
        destination.m21 = m01 * raw8 + m11 * raw9 + m21 * raw10
        destination.m22 = m02 * raw8 + m12 * raw9 + m22 * raw10
        destination.m23 = m03 * raw8 + m13 * raw9 + m23 * raw10
        destination.m00 = final0
        destination.m01 = final1
        destination.m02 = final2
        destination.m03 = final3
        destination.m10 = final4
        destination.m11 = final5
        destination.m12 = final6
        destination.m13 = final7

        return destination
    }

//    fun rotate(direction: Vector3f): Matrix4f {
//        val xAxis = Vector3f(0.0f, 1.0f, 0.0f).cross(direction)
//        xAxis.normalize()
//
//        val yAxis = direction.cross(xAxis)
//        yAxis.normalize()
//
//        m00 = xAxis.x
//        m10 = yAxis.x
//        m20 = direction.x
//
//        m01 = xAxis.y
//        m11 = yAxis.y
//        m21 = direction.y
//
//        m02 = xAxis.z
//        m12 = yAxis.z
//        m22 = direction.z
//
//        return this
//    }

    fun multiply(other: Matrix4f): Matrix4f {
        return multiply(other, this)
    }
    
    fun multiply(other: Matrix4f, destination: Matrix4f): Matrix4f {
        val m00 = this.m00 * other.m00 + this.m10 * other.m01 + this.m20 * other.m02 + this.m30 * other.m03
        val m01 = this.m01 * other.m00 + this.m11 * other.m01 + this.m21 * other.m02 + this.m31 * other.m03
		val m02 = this.m02 * other.m00 + this.m12 * other.m01 + this.m22 * other.m02 + this.m32 * other.m03
		val m03 = this.m03 * other.m00 + this.m13 * other.m01 + this.m23 * other.m02 + this.m33 * other.m03
		val m10 = this.m00 * other.m10 + this.m10 * other.m11 + this.m20 * other.m12 + this.m30 * other.m13
		val m11 = this.m01 * other.m10 + this.m11 * other.m11 + this.m21 * other.m12 + this.m31 * other.m13
		val m12 = this.m02 * other.m10 + this.m12 * other.m11 + this.m22 * other.m12 + this.m32 * other.m13
		val m13 = this.m03 * other.m10 + this.m13 * other.m11 + this.m23 * other.m12 + this.m33 * other.m13
		val m20 = this.m00 * other.m20 + this.m10 * other.m21 + this.m20 * other.m22 + this.m30 * other.m23
		val m21 = this.m01 * other.m20 + this.m11 * other.m21 + this.m21 * other.m22 + this.m31 * other.m23
		val m22 = this.m02 * other.m20 + this.m12 * other.m21 + this.m22 * other.m22 + this.m32 * other.m23
		val m23 = this.m03 * other.m20 + this.m13 * other.m21 + this.m23 * other.m22 + this.m33 * other.m23
		val m30 = this.m00 * other.m30 + this.m10 * other.m31 + this.m20 * other.m32 + this.m30 * other.m33
		val m31 = this.m01 * other.m30 + this.m11 * other.m31 + this.m21 * other.m32 + this.m31 * other.m33
		val m32 = this.m02 * other.m30 + this.m12 * other.m31 + this.m22 * other.m32 + this.m32 * other.m33
		val m33 = this.m03 * other.m30 + this.m13 * other.m31 + this.m23 * other.m32 + this.m33 * other.m33

        destination.m00 = m00
        destination.m10 = m10
        destination.m20 = m20
        destination.m30 = m30
        destination.m01 = m01
        destination.m11 = m11
        destination.m21 = m21
        destination.m31 = m31
        destination.m02 = m02
        destination.m12 = m12
        destination.m22 = m22
        destination.m32 = m32
        destination.m03 = m03
        destination.m13 = m13
        destination.m23 = m23
        destination.m33 = m33

        return destination
    }

    fun determinant4x4(): Float {
        var d = m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33)
        d -= m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33)
        d += m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33)
        d -= m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32)

        return d
    }

    fun determinant3x3(m00: Float, m10: Float, m20: Float, m01: Float, m11: Float, m21: Float, m02: Float, m12: Float, m22: Float): Float {
        return m00 * (m11 * m22 - m12 * m21) + m01 * (m12 * m20 - m10 * m22) + m02 * (m10 * m21 - m11 * m20)
    }

    fun invert(destination: Matrix4f): Matrix4f {
        val determinant = determinant4x4()

        if (determinant != 0.0f) {
            val invertedDeterminant = 1.0f / determinant

            // I also have to rewrite this
            val t00 =  determinant3x3(m11, m12, m13, m21, m22, m23, m31, m32, m33);
			val t01 = -determinant3x3(m10, m12, m13, m20, m22, m23, m30, m32, m33);
			val t02 =  determinant3x3(m10, m11, m13, m20, m21, m23, m30, m31, m33);
			val t03 = -determinant3x3(m10, m11, m12, m20, m21, m22, m30, m31, m32);
			// second row
			val t10 = -determinant3x3(m01, m02, m03, m21, m22, m23, m31, m32, m33);
			val t11 =  determinant3x3(m00, m02, m03, m20, m22, m23, m30, m32, m33);
			val t12 = -determinant3x3(m00, m01, m03, m20, m21, m23, m30, m31, m33);
			val t13 =  determinant3x3(m00, m01, m02, m20, m21, m22, m30, m31, m32);
			// third row
			val t20 =  determinant3x3(m01, m02, m03, m11, m12, m13, m31, m32, m33);
			val t21 = -determinant3x3(m00, m02, m03, m10, m12, m13, m30, m32, m33);
			val t22 =  determinant3x3(m00, m01, m03, m10, m11, m13, m30, m31, m33);
			val t23 = -determinant3x3(m00, m01, m02, m10, m11, m12, m30, m31, m32);
			// fourth row
			val t30 = -determinant3x3(m01, m02, m03, m11, m12, m13, m21, m22, m23);
			val t31 =  determinant3x3(m00, m02, m03, m10, m12, m13, m20, m22, m23);
			val t32 = -determinant3x3(m00, m01, m03, m10, m11, m13, m20, m21, m23);
			val t33 =  determinant3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);

            destination.m00 = t00*invertedDeterminant;
            destination.m11 = t11*invertedDeterminant;
            destination.m22 = t22*invertedDeterminant;
            destination.m33 = t33*invertedDeterminant;
            destination.m01 = t10*invertedDeterminant;
            destination.m10 = t01*invertedDeterminant;
            destination.m20 = t02*invertedDeterminant;
            destination.m02 = t20*invertedDeterminant;
            destination.m12 = t21*invertedDeterminant;
            destination.m21 = t12*invertedDeterminant;
            destination.m03 = t30*invertedDeterminant;
            destination.m30 = t03*invertedDeterminant;
            destination.m13 = t31*invertedDeterminant;
            destination.m31 = t13*invertedDeterminant;
            destination.m32 = t23*invertedDeterminant;
            destination.m23 = t32*invertedDeterminant;
        }

        return destination
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

    fun fromArray(array: Array<Float>): Matrix4f {
//        for (i in 0 until array.size) {
//            floatArray[i] = array[i]
//        }

        m00 = array[0]
        m10 = array[1]
        m20 = array[2]
        m30 = array[3]
        m01 = array[4]
        m11 = array[5]
        m21 = array[6]
        m31 = array[7]
        m02 = array[8]
        m12 = array[9]
        m22 = array[10]
        m32 = array[11]
        m03 = array[12]
        m13 = array[13]
        m23 = array[14]
        m33 = array[15]

        return this
    }

    fun fromQuaternion(quat: Quaternion): Matrix4f {
        quat.toRotationMatrix(this)

        return this
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

    override fun toString(): String {
        return "$m00 | $m10 | $m20 | $m30\n$m01 | $m11 | $m21 | $m31\n$m02 | $m12 | $m22 | $m32\n$m03 | $m13 | $m23 | $m33\n"
    }
}