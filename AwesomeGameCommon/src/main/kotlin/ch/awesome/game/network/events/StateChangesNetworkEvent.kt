package ch.awesome.game.network.events

import ch.awesome.game.network.INetworkEvent
import ch.awesome.game.network.NetworkEvent
import ch.awesome.game.network.NetworkEventType
import ch.awesome.game.utils.SmartChange

interface IStateChangesNetworkEvent: INetworkEvent<List<SmartChange>>

class StateChangesNetworkEvent(changes: List<SmartChange>)
    : NetworkEvent<List<SmartChange>>(NetworkEventType.STATE_CHANGES, changes)