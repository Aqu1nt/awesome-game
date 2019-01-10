package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.IMovingBaseObject
import ch.awesome.game.server.utils.SmartProperty

open class MovingBaseObject(unitsPerSecond: Float = 1f,
                            rotationUnitsPerSecond: Float = 1f,
                            scaleUnitsPerSecond: Float = 1f) : BaseObject(), IMovingBaseObject<Vector3f> {

    // Translation
    override var velocity: Vector3f by SmartProperty(Vector3f())
    override var unitPerSecond: Float by SmartProperty(unitsPerSecond)

    // Rotation
    override var rotationVelocity: Vector3f by SmartProperty(Vector3f())
    override var rotationUnitPerSecond: Float by SmartProperty(rotationUnitsPerSecond)

    // Scale
    override var scaleVelocity: Vector3f by SmartProperty(Vector3f())
    override var scaleUnitPerSecond: Float by SmartProperty(scaleUnitsPerSecond)

    override fun update(tpf: Float) {
        if (unitPerSecond != 0f && (velocity.x != 0f || velocity.y != 0f || velocity.z != 0f)) {
            position += Vector3f(
                    velocity.x * tpf * unitPerSecond,
                    velocity.y * tpf * unitPerSecond,
                    velocity.z * tpf * unitPerSecond
            )
        }
        if (rotationUnitPerSecond != 0f && (rotationVelocity.x != 0f || rotationVelocity.y != 0f || rotationVelocity.z != 0f)) {
            rotation += Vector3f(
                    rotationVelocity.x * tpf * rotationUnitPerSecond,
                    rotationVelocity.y * tpf * rotationUnitPerSecond,
                    rotationVelocity.z * tpf * rotationUnitPerSecond
            )
        }
        if (scaleUnitPerSecond != 0f && (scaleVelocity.x != 0f || scaleVelocity.y != 0f || scaleVelocity.z != 0f)) {
            scale += Vector3f(
                    scaleVelocity.x * tpf * scaleUnitPerSecond,
                    scaleVelocity.y * tpf * scaleUnitPerSecond,
                    scaleVelocity.z * tpf * scaleUnitPerSecond
            )
        }
        super.update(tpf)
    }
}