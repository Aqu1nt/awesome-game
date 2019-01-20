package ch.awesome.game.client.rendering

import ch.awesome.game.client.objects.CPlayer
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.math.toRadians
import kotlin.math.cos
import kotlin.math.sin

class Camera {

    var position = Vector3f(0.0f, 0.0f, 0.0f)
    var pitch = 30.0f
    var yaw = 0.0f
    var roll = 0.0f

    var direction = Vector3f(0.0f, 0.0f, 0.0f)

    var distanceFromPlayer = 60.0f
    var angleAround = 0.0f

    fun set(x: Float, y: Float, z: Float, pitch: Float, yaw: Float, roll: Float) {
        position.x = x
        position.y = y
        position.z = z
        this.pitch = pitch
        this.yaw = yaw
        this.roll = roll
    }

    fun lookAt(x: Float, y: Float, z: Float, targetX: Float, targetY: Float, targetZ: Float) {
        position.x = x
        position.y = y
        position.z = z

        direction = Vector3f(targetX, targetY, targetZ) - Vector3f(x, y, z)
    }

    fun update(player: CPlayer) {
        if (pitch < -90.0f) pitch = -90.0f
        else if (pitch > 90.0f) pitch = 90.0f

        if (distanceFromPlayer < 10.0f) distanceFromPlayer = 10.0f
        else if (distanceFromPlayer > 150.0f) distanceFromPlayer = 150.0f

        val horizontalDistance = distanceFromPlayer * cos(toRadians(pitch))
        val verticalDistance = distanceFromPlayer * sin(toRadians(pitch))
        val rotation = angleAround

        val xOffset = horizontalDistance * sin(toRadians(rotation))
        val zOffset = horizontalDistance * cos(toRadians(rotation))

        position.x = player.worldTranslation.x - xOffset
        position.y = player.worldTranslation.y + verticalDistance
        position.z = player.worldTranslation.z - zOffset

        yaw = 180 - rotation
    }
}