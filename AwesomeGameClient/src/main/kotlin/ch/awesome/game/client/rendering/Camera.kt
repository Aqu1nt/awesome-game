package ch.awesome.game.client.rendering

import ch.awesome.game.common.math.Vector3f

class Camera {

    var position = Vector3f(0.0f, 0.0f, 0.0f)
    var pitch = 0.0f
    var yaw = 0.0f
    var roll = 0.0f

    fun lookAt(x: Float, y: Float, z: Float, pitch: Float, yaw: Float, roll: Float) {
        position.x = x
        position.y = y
        position.z = z
        this.pitch = pitch
        this.yaw = yaw
        this.roll = roll
    }
}