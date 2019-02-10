package ch.awesome.game.common.network.events

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import kotlinx.serialization.Serializable

@Serializable
data class Cheat(val name: String)

@Serializable
class CheatNetworkEvent: INetworkEvent<Cheat?> {

    override var payload: Cheat? = null

    override val type: NetworkEventType
        get() = NetworkEventType.CHEAT
}