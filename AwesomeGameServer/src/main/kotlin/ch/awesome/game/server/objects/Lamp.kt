package ch.awesome.game.server.objects

import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.server.utils.withSmartProperties

class Lamp(): BaseObject() {

    var color: Vector3f by SmartProperty(Vector3f(1.0f, 1.0f, 1.0f))

    init { withSmartProperties() }
}