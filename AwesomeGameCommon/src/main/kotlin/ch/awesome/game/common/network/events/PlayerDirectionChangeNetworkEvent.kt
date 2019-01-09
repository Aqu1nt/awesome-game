package ch.awesome.game.common.network.events

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDirectionChange(val x: Float, val y: Float, val z: Float)

@Serializable
class PlayerDirectionChangeNetworkEvent: INetworkEvent<PlayerDirectionChange?> {

    override var payload: PlayerDirectionChange? = null

    override val type: NetworkEventType
        get() = NetworkEventType.PLAYER_DIRECTION_CHANGE
}