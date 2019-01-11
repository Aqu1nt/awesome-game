package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.Scene
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.IWorld

class World(state: dynamic): Scene(state), IWorld<IVector3f> {

}