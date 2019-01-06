package ch.awesome.game.utils

data class Vector2f (val x: Float, val y: Float) {

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
}