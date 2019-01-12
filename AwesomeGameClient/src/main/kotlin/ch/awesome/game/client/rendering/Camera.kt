package ch.awesome.game.client.rendering

import ch.awesome.game.common.math.Vector3f

class Camera {

    var position = Vector3f(0.0f, 0.0f, 0.0f)
    var pitch = 0.0f
    var yaw = 0.0f
    var roll = 0.0f

    var direction = Vector3f(0.0f, 0.0f, 0.0f)

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
}