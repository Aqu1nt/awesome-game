package ch.awesome.game.server.objects

import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.common.utils.Vector3f

open class MovingBaseObject(unitsPerSecond: Float = 1.0f): BaseObject() {

    var velocity: Vector3f by SmartProperty(Vector3f())
    var unitPerSecond: Float by SmartProperty(unitsPerSecond)

    override fun update(tpf: Float) {
        position += Vector3f(velocity.x * tpf * unitPerSecond, velocity.y * tpf * unitPerSecond, velocity.z * tpf * unitPerSecond)
        super.update(tpf)
    }
}