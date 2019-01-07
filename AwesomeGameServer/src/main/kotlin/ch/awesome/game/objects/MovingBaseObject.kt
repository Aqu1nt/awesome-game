package ch.awesome.game.objects

import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.Vector3f

open class MovingBaseObject(unitsPerSecond: Float = 1.0f): BaseObject() {

    var position: Vector3f by SmartProperty(Vector3f())
    var velocity: Vector3f by SmartProperty(Vector3f())
    var unitPerSecond: Float by SmartProperty(unitsPerSecond)

    override fun update(tpf: Float) {
        position += Vector3f(velocity.x * tpf * unitPerSecond, velocity.y * tpf * unitPerSecond, velocity.z * tpf * unitPerSecond)
        super.update(tpf)
    }
}