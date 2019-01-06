package ch.awesome.game.network.events

import ch.awesome.game.network.INetworkEvent
import ch.awesome.game.network.NetworkEvent
import ch.awesome.game.network.NetworkEventType

interface IPlayerJoinedGameInfo {
    val playerId: String
}

interface IPlayerJoinedGameNetworkEvent: INetworkEvent<IPlayerJoinedGameInfo>


class PlayerJoinedGameInfo(
        override val playerId: String
): IPlayerJoinedGameInfo

class PlayerJoinedGameNetworkEvent(info: PlayerJoinedGameInfo)
    : NetworkEvent<IPlayerJoinedGameInfo>(NetworkEventType.PLAYER_JOINED_GAME, info), IPlayerJoinedGameNetworkEvent