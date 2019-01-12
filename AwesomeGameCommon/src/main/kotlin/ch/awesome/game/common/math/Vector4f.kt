package ch.awesome.game.common.math

interface IVector4f {
    var x: Float
    var y: Float
    var z: Float
    var w: Float
}

data class Vector4f (override var x: Float = 0f,
                     override var y: Float = 0f,
                     override var z: Float = 0f,
                     override var w: Float = 0f): IVector4f {

    constructor(vector3f: IVector3f, w: Float): this(vector3f.x, vector3f.y, vector3f.z, w)

    constructor(vector4f: IVector4f): this(vector4f.x, vector4f.y, vector4f.z, vector4f.w)

    operator fun plus(other: Float): Vector4f {
        return Vector4f(x + other, y + other, z + other, w + other)
    }

    operator fun plus(other: IVector4f): Vector4f {
        return Vector4f(x + other.x, y + other.y, z + other.z, w + other.w)
    }

    operator fun minus(other: Float): Vector4f {
        return Vector4f(x - other, y - other, z - other, w - other)
    }

    operator fun minus(other: IVector4f): Vector4f {
        return Vector4f(x - other.x, y - other.y, z - other.z, w - other.w)
    }

    operator fun times(other: Float): Vector4f {
        return Vector4f(x * other, y * other, z * other, w * other)
    }

    operator fun times(other: IVector4f): Vector4f {
        return Vector4f(x * other.x, y * other.y, z * other.z, w * other.w)
    }

    operator fun div(other: Float): Vector4f {
        return Vector4f(x / other, y / other, z / other, w / other)
    }

    operator fun div(other: IVector4f): Vector4f {
        return Vector4f(x / other.x, y / other.y, z / other.z, w / other.w)
    }
}