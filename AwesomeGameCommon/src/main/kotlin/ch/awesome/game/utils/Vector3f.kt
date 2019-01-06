package ch.awesome.game.utils

data class Vector3f (var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    operator fun plus(other: Vector3f): Vector3f {
        return Vector3f(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3f): Vector3f {
        return Vector3f(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(other: Vector3f): Vector3f {
        return Vector3f(x * other.x, y * other.y, z * other.z)
    }

    operator fun div(other: Vector3f): Vector3f {
        return Vector3f(x / other.x, y / other.y, z / other.z)
    }

    fun toFloatArray(): Array<Float> {
        return arrayOf(x, y, z)
    }
}