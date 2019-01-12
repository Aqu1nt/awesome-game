package ch.awesome.game.client.objects.base

import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.base.IScene

open class CScene(state: dynamic, var clearColor: IVector3f = Vector3f(0f, 0f, 0f))
    : GameNode(state), IScene<IVector3f> {

    override var ambientLight: Float by StateProperty()
}