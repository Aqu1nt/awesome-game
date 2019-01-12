package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.network.events.GameStateNode
import ch.awesome.game.common.network.events.IGameStateNode
import ch.awesome.game.common.network.events.StateChangesNetworkEvent
import ch.awesome.game.common.objects.IWorld
import ch.awesome.game.server.objects.base.SBaseObject
import ch.awesome.game.server.objects.base.SScene
import ch.awesome.game.server.utils.withSmartProperties

class SWorld: SScene("WORLD"), IWorld<Vector3f> {

    companion object {
        const val SEND_CHANGES_INTERVAL = 50L
    }

    private var lastSendChangesTimestamp = 0L

    init {
        ambientLight = 0.2f
        withSmartProperties()
    }

    fun state(): IGameStateNode {
        fun convertItem(item: SBaseObject): IGameStateNode {
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
                    if (child is SPlayer) {
                        child.sendEvent(networkEvent)
                    }
                }
            }
            lastSendChangesTimestamp = System.currentTimeMillis()
        }
    }
}