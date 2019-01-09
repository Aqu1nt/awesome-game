package ch.awesome.game.objects

import ch.awesome.game.network.GameWebSocketHandler
import ch.awesome.game.network.NetworkEvent
import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.Vector3f
import ch.awesome.game.utils.withSmartProperties
import com.fasterxml.jackson.annotation.JsonIgnore

class Lamp(): BaseObject() {

    var color: Vector3f by SmartProperty(Vector3f(1.0f, 1.0f, 1.0f))

    init { withSmartProperties() }
}