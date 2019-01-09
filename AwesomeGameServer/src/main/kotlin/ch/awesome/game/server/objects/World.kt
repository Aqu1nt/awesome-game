package ch.awesome.game.server.objects

import ch.awesome.game.server.instance.GAME
import ch.awesome.game.common.network.events.GameStateNode
import ch.awesome.game.common.network.events.IGameStateNode
import ch.awesome.game.common.network.events.StateChangesNetworkEvent
import ch.awesome.game.server.utils.SmartTreeItem
import ch.awesome.game.server.utils.withSmartProperties

class World: BaseObject("WORLD") {

    companion object {
        const val SEND_CHANGES_INTERVAL = 50L
    }

    private var lastSendChangesTimestamp = 0L

    init {
        withSmartProperties()
    }

    fun state(): IGameStateNode {
        fun convertItem(item: SmartTreeItem): IGameStateNode {
            return GameStateNode(
                    id = item.id,
                    data = item,
                    children = item.children().map { convertItem(it) }.toTypedArray())
        }
        return convertItem(this)
    }

    override fun afterUpdate() {
        super.afterUpdate()

        // Send state
        if (System.currentTimeMillis() > lastSendChangesTimestamp + SEND_CHANGES_INTERVAL) {
            val changes = GAME.world.fetchAndResetChanges()
            if (changes.isNotEmpty()) {
                val networkEvent = StateChangesNetworkEvent(changes)
                for (child in children()) {
                    if (child is Player) {
                        child.sendEvent(networkEvent)
                    }
                }
            }
            lastSendChangesTimestamp = System.currentTimeMillis()
        }
    }
}