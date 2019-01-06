package ch.awesome.game.network.events

import ch.awesome.game.network.INetworkEvent
import ch.awesome.game.network.NetworkEvent
import ch.awesome.game.network.NetworkEventType

interface IGameStateNode {
    val id: String
    val children: Array<IGameStateNode>
    val data: Any
}

interface IStateNetworkEvent: INetworkEvent<IGameStateNode>


class GameStateNode(override val id: String,
                    override val data: Any,
                    override val children: Array<IGameStateNode>): IGameStateNode

class StateNetworkEvent(state: IGameStateNode)
    : NetworkEvent<IGameStateNode>(NetworkEventType.STATE, state), IStateNetworkEvent