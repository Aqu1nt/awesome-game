package ch.awesome.game.server.objects

import ch.awesome.game.server.network.GameWebSocketHandler
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.server.utils.withSmartProperties
import com.fasterxml.jackson.annotation.JsonIgnore

class Player(initName: String? = null, initAge: Int? = null): MovingBaseObject(10f) {

    var name: String? by SmartProperty(initName)
    var age: Int? by SmartProperty(initAge)

    @JsonIgnore
    var webSocketHandler: GameWebSocketHandler? = null

    init { withSmartProperties() }

    fun sendEvent(event: NetworkEvent<*>) {
        webSocketHandler?.sendEvent(event)
    }
}