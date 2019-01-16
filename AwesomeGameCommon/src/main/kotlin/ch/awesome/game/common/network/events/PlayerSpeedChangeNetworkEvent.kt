package ch.awesome.game.common.network.events

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
data class PlayerSpeedChange(val unitPerSec: Float)

@Serializable
class PlayerSpeedChangeNetworkEvent: INetworkEvent<PlayerSpeedChange?> {

    override var payload: PlayerSpeedChange? = null

    override val type: NetworkEventType
        get() = NetworkEventType.PLAYER_SPEED_CHANGE
}