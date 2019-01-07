package ch.awesome.game.network.events

import ch.awesome.game.network.INetworkEvent
import ch.awesome.game.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
class PingNetworkEvent: INetworkEvent<Long?> {

    override var payload: Long? = 0

    override val type: NetworkEventType
        get() = NetworkEventType.PING
}