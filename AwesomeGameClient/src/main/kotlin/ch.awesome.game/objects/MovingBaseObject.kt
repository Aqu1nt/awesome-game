package ch.awesome.game.objects

import ch.awesome.game.state.GameNode
import ch.awesome.game.state.StateProperty
import ch.awesome.game.utils.IVector3f
import ch.awesome.game.utils.Vector3f

open class MovingBaseObject(state: dynamic): GameNode(state), IMovingBaseObject {

    final override var position: IVector3f by StateProperty()
    override var velocity: IVector3f by StateProperty()
    override var unitPerSecond: Float by StateProperty()

    var localPosition: IVector3f = Vector3f(position)

    override fun update(tpf: Float) {
        localPosition.x += velocity.x * unitPerSecond * tpf
        localPosition.y += velocity.y * unitPerSecond * tpf
        localPosition.z += velocity.z * unitPerSecond * tpf
        adjustVector3f(localPosition, position, 0.1f)
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