package ch.awesome.game.objects

import ch.awesome.game.instance.GAME
import ch.awesome.game.network.events.GameStateNode
import ch.awesome.game.network.events.IGameStateNode
import ch.awesome.game.network.events.StateChangesNetworkEvent
import ch.awesome.game.utils.SmartTreeItem
import ch.awesome.game.utils.withSmartProperties

class World: BaseObject("WORLD") {
    init { withSmartProperties() }

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
        val changes = GAME.world.fetchAndResetChanges()
        if (changes.isNotEmpty()) {
            val networkEvent = StateChangesNetworkEvent(changes)
            for (child in children()) {
                if (child is Player) {
                    child.sendEvent(networkEvent)
                }
            }
        }
    }
}