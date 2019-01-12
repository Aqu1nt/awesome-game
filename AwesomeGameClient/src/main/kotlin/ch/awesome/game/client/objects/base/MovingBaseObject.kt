package ch.awesome.game.client.objects.base

import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.base.IMovingBaseObject

open class MovingBaseObject(state: dynamic): GameNode(state), IMovingBaseObject<IVector3f> {

    var simulateTranslation: Boolean = true
        set(value) {
            if (value) {
                localPosition = position
            }
            field = value
        }

    var simulateRotation: Boolean = true
        set(value) {
            if (value) {
                localRotation = rotation
            }
            field = value
        }

    var simulateScale: Boolean = false
        set(value) {
            if (value) {
                localScale = scale
            }
            field = value
        }

    // Translation
    override var velocity: IVector3f by StateProperty()
    override var unitPerSecond: Float by StateProperty()
    var localPosition: IVector3f = Vector3f(position)

    // Rotation
    override var rotationVelocity: IVector3f by StateProperty()
    override var rotationUnitPerSecond: Float by StateProperty()
    var localRotation: IVector3f = Vector3f(rotation)
    
    // Scale
    override var scaleVelocity: IVector3f by StateProperty()
    override var scaleUnitPerSecond: Float by StateProperty()
    var localScale: IVector3f = Vector3f(scale)
    
    override fun actualPosition(): IVector3f {
        return if (simulateTranslation) localPosition else position
    }

    override fun actualRotation(): IVector3f {
        return if (simulateRotation) localRotation else rotation
    }

    override fun actualScale(): IVector3f {
        return if (simulateTranslation) localScale else scale
    }

    override fun update(tpf: Float) {
        // Update translation
        if (simulateTranslation && unitPerSecond != 0f && (velocity.x != 0f || velocity.y != 0f || velocity.z != 0f)) {
            localPosition.x += velocity.x * unitPerSecond * tpf
            localPosition.y += velocity.y * unitPerSecond * tpf
            localPosition.z += velocity.z * unitPerSecond * tpf
            adjustVector3f(localPosition, position, 0.2f)
        } else {
            localPosition = position
        }

        // Update rotation
        if (simulateRotation && unitPerSecond != 0f && (rotationVelocity.x != 0f || rotationVelocity.y != 0f || rotationVelocity.z != 0f)) {
            localRotation.x += rotationVelocity.x * rotationUnitPerSecond * tpf
            localRotation.y += rotationVelocity.y * rotationUnitPerSecond * tpf
            localRotation.z += rotationVelocity.z * rotationUnitPerSecond * tpf
            adjustVector3f(localRotation, rotation, 0.2f)
        } else {
            localRotation = rotation
        }

        // Update scale
        if (simulateScale && scaleUnitPerSecond != 0f && (scaleVelocity.x != 0f || scaleVelocity.y != 0f || scaleVelocity.z != 0f)) {
            localScale.x += scaleVelocity.x * scaleUnitPerSecond * tpf
            localScale.y += scaleVelocity.y * scaleUnitPerSecond * tpf
            localScale.z += scaleVelocity.z * scaleUnitPerSecond * tpf
            adjustVector3f(localScale, localRotation, 0.2f)
        } else {
            localScale = scale
        }

        super.update(tpf)
    }

    private fun adjustVector3f(source: IVector3f, target: IVector3f, percent: Float) {
        val diffX = source.x - target.x
        val diffY = source.y - target.y
        val diffZ = source.z - target.z
        source.x -= diffX * percent
        source.y -= diffY * percent
        source.z -= diffZ * percent
    }
}