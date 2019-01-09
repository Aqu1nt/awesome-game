package ch.awesome.game.server.objects

import ch.awesome.game.server.network.GameWebSocketHandler
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.common.utils.Vector3f
import ch.awesome.game.server.utils.withSmartProperties
import com.fasterxml.jackson.annotation.JsonIgnore

class Lamp(): BaseObject() {

    var color: Vector3f by SmartProperty(Vector3f(1.0f, 1.0f, 1.0f))

    init { withSmartProperties() }
}