package ch.awesome.game.utils

interface IVector3f {
    var x: Float
    var y: Float
    var z: Float
}

data class Vector3f (override var x: Float = 0f,
                     override var y: Float = 0f,
                     override var z: Float = 0f): IVector3f {

    constructor(vector3f: IVector3f): this(vector3f.x, vector3f.y, vector3f.z)

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