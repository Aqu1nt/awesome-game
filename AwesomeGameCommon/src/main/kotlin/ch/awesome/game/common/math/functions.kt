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

fun inPixelWidth(size: Float, displayWidth: Int): Float {
    return size * displayWidth
}

fun inPixelHeight(size: Float, displayHeight: Int): Float {
    return size * displayHeight
}

fun inScreenWidth(size: Float, displayWidth: Int): Float {
    return 1.0f / (displayWidth / size)
}

fun inScreenHeight(size: Float, displayHeight: Int): Float {
    return 1.0f / (displayHeight / size)
}