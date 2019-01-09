package ch.awesome.game.common.network.events

import ch.awesome.game.common.network.INetworkEvent
import ch.awesome.game.common.network.NetworkEvent
import ch.awesome.game.common.network.NetworkEventType
import ch.awesome.game.common.utils.SmartChange

interface IStateChangesNetworkEvent: INetworkEvent<List<SmartChange>>

class StateChangesNetworkEvent(changes: List<SmartChange>)
    : NetworkEvent<List<SmartChange>>(NetworkEventType.STATE_CHANGES, changes), IStateChangesNetworkEvent