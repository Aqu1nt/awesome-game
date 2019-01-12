package ch.awesome.game.common.math

import kotlin.math.PI
import kotlin.math.sqrt

fun invSqrt(fValue: Float): Float {
    return (1.0f / sqrt(fValue.toDouble())).toFloat()
}

fun toRadians(degrees: Float): Float {
    return degrees / 180.0f * PI.toFloat()
}

fun toDegrees(radians: Float): Float {
    return radians * 180.0f / PI.toFloat()
}