package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CScene
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.IWorld

class CWorld(state: dynamic): CScene(state, Vector3f(0f, 0f, .1f)), IWorld<IVector3f> {

}