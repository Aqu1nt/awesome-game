package ch.awesome.game.server.objects.base

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.base.IScene
import ch.awesome.game.server.utils.SmartProperty

open class SScene(id: String): SBaseObject(id), IScene<Vector3f> {

    override var ambientLight: Float by SmartProperty(0.0f)
}