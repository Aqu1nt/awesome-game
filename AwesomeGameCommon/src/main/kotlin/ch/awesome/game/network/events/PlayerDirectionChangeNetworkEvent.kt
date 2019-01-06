package ch.awesome.game.network.events

import ch.awesome.game.network.INetworkEvent
import ch.awesome.game.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDirectionChange(val x: Float, val y: Float, val z: Float)

@Serializable
class PlayerDirectionChangeNetworkEvent: INetworkEvent<PlayerDirectionChange?> {

    override var payload: PlayerDirectionChange? = null

    override val type: NetworkEventType
        get() = NetworkEventType.PLAYER_DIRECTION_CHANGE
}