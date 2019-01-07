package ch.awesome.game.objects

import ch.awesome.game.network.GameWebSocketHandler
import ch.awesome.game.network.NetworkEvent
import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.withSmartProperties
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