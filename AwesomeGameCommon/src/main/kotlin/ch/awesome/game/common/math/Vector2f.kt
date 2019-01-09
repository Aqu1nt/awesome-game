package ch.awesome.game.common.math

interface IVector2f {
    var x: Float
    var y: Float
}

data class Vector2f (override var x: Float = 0f,
                     override var y: Float = 0f): IVector2f {

    operator fun plus(other: Vector2f): Vector2f {
        return Vector2f(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2f): Vector2f {
        return Vector2f(x - other.x, y - other.y)
    }

    operator fun times(other: Vector2f): Vector2f {
        return Vector2f(x * other.x, y * other.y)
    }

    operator fun div(other: Vector2f): Vector2f {
        return Vector2f(x / other.x, y / other.y)
    }

    fun toFloatArray(): Array<Float> {
        return arrayOf(x, y)
    }
}