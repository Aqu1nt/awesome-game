package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.common.objects.IPlayer
import ch.awesome.game.server.network.GameWebSocketHandler
import ch.awesome.game.server.objects.base.MovingBaseObject
import ch.awesome.game.server.utils.withSmartProperties
import com.fasterxml.jackson.annotation.JsonIgnore

class Player: MovingBaseObject(20f), IPlayer<Vector3f> {

    @JsonIgnore
    var webSocketHandler: GameWebSocketHandler? = null

    init {
        withSmartProperties()
    }

    fun sendEvent(event: NetworkEvent<*>) {
        webSocketHandler?.sendEvent(event)
    }
}