package ch.awesome.game.common.network.events

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
class PlayerShootNetworkEvent: INetworkEvent<Int?> {

    override var payload: Int? = null

    override val type: NetworkEventType
        get() = NetworkEventType.PLAYER_SHOOT
}