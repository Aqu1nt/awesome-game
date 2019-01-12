package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.network.events.GameStateNode
import ch.awesome.game.common.network.events.IGameStateNode
import ch.awesome.game.common.network.events.StateChangesNetworkEvent
import ch.awesome.game.common.objects.IWorld
import ch.awesome.game.server.objects.base.Scene
import ch.awesome.game.server.utils.SmartTreeItem
import ch.awesome.game.server.utils.withSmartProperties

class World: Scene("WORLD"), IWorld<Vector3f> {

    companion object {
        const val SEND_CHANGES_INTERVAL = 50L
    }

    private var lastSendChangesTimestamp = 0L

    init {
        ambientLight = 0.2f
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
            val changes = fetchAndResetChanges()
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