package ch.awesome.game.client.objects.base

import ch.awesome.game.client.state.GameNode
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.base.IGroup

class Group(state: dynamic) : GameNode(state), IGroup<IVector3f> {

}