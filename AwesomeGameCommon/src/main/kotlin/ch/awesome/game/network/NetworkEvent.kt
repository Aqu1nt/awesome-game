package ch.awesome.game.network

enum class NetworkEventType {
    STATE,
    STATE_CHANGES
}

interface INetworkEvent<T> {
    val type: NetworkEventType
    val payload: T
}

open class NetworkEvent<T>(
        override val type: NetworkEventType,
        override val payload: T
): INetworkEvent<T>