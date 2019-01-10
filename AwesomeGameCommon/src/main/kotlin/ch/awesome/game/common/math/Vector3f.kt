package ch.awesome.game.common.math

import kotlin.math.sqrt

interface IVector3f {
    var x: Float
    var y: Float
    var z: Float
}

data class Vector3f (override var x: Float = 0f,
                     override var y: Float = 0f,
                     override var z: Float = 0f): IVector3f {

    constructor(vector3f: IVector3f): this(vector3f.x, vector3f.y, vector3f.z)

    operator fun plus(other: Float): Vector3f {
        return Vector3f(x + other, y + other, z + other)
    }

    operator fun plus(other: IVector3f): Vector3f {
        return Vector3f(x + other.x, y + other.y, z + other.z)
    }

    fun addLocal(vec: Vector3f): Vector3f {
        x += vec.x
        y += vec.y
        z += vec.z
        return this
    }

    operator fun minus(other: Float): Vector3f {
        return Vector3f(x - other, y - other, z - other)
    }

    operator fun minus(other: IVector3f): Vector3f {
        return Vector3f(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(other: Float): Vector3f {
        return Vector3f(x * other, y * other, z * other)
    }

    operator fun times(other: IVector3f): Vector3f {
        return Vector3f(x * other.x, y * other.y, z * other.z)
    }

    fun multLocal(scalar: Float): Vector3f {
        x *= scalar
        y *= scalar
        z *= scalar
        return this
    }

    fun multLocal(vec: IVector3f): Vector3f {
        x *= vec.x
        y *= vec.y
        z *= vec.z
        return this
    }

    operator fun div(other: Float): Vector3f {
        return Vector3f(x / other, y / other, z / other)
    }

    operator fun div(other: IVector3f): Vector3f {
        return Vector3f(x / other.x, y / other.y, z / other.z)
    }

    fun length(): Float {
        return sqrt(lengthSquared())
    }

    fun lengthSquared(): Float {
        return x * x + y * y + z * z
    }

    fun distanceSquared(v: Vector3f): Float {
        val dx = (x - v.x).toDouble()
        val dy = (y - v.y).toDouble()
        val dz = (z - v.z).toDouble()
        return (dx * dx + dy * dy + dz * dz).toFloat()
    }

    /**
     * `distance` calculates the distance between this vector and
     * vector v.
     *
     * @param v the second vector to determine the distance.
     * @return the distance between the two vectors.
     */
    fun distance(v: Vector3f): Float {
        return sqrt(distanceSquared(v))
    }

    fun normalize(): Vector3f {
        val length = length()
        return Vector3f(x / length, y / length, z / length)
    }

    fun normalizeLocal(): Vector3f {
        var length = x * x + y * y + z * z
        if (length != 1f && length != 0f) {
            length = 1.0f / sqrt(length)
            x *= length
            y *= length
            z *= length
        }
        return this
    }

    fun toFloatArray(): Array<Float> {
        return arrayOf(x, y, z)
    }

    fun set(vec: IVector3f) {
        set(vec.x, vec.y, vec.z)
    }

    fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }
}