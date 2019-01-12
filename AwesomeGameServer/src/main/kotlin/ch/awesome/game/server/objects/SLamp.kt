package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.ILamp
import ch.awesome.game.server.objects.base.SBaseObject
import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.server.utils.withSmartProperties

class SLamp: SBaseObject(), ILamp<Vector3f> {

    override var color: Vector3f by SmartProperty(Vector3f(1.0f, 1.0f, 1.0f))

    init { withSmartProperties() }
}