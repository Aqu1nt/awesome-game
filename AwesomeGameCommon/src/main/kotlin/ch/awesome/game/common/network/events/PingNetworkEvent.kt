package ch.awesome.game.common.network.events

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
class PingNetworkEvent: INetworkEvent<Long?> {

    override var payload: Long? = 0

    override val type: NetworkEventType
        get() = NetworkEventType.PING
}