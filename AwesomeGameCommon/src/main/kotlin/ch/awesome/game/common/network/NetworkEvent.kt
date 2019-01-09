package ch.awesome.game.common.network

enum class NetworkEventType {
    STATE,
    STATE_CHANGES,
    PLAYER_JOINED_GAME,
    PLAYER_DIRECTION_CHANGE,
    PING
}

interface INetworkEvent<T> {
    val type: NetworkEventType
    val payload: T
}

open class NetworkEvent<T>(
        override val type: NetworkEventType,
        override val payload: T
): INetworkEvent<T>