package ch.awesome.game.utils

import java.util.*

open class SmartTreeItem(val id: String = UUID.randomUUID().toString()) {

    private var parent: SmartTreeItem? = null
    private var changes: MutableSet<SmartChange> = mutableSetOf()
    private var children: MutableSet<SmartTreeItem> = mutableSetOf()

    fun addChild(child: SmartTreeItem) {
        synchronized(children) {
            child.parent = this
            children.add(child)
            changes.add(SmartChange(id, child, SmartChangeType.CHILDREN_ADD))
        }
    }

    fun removeChild(child: SmartTreeItem) {
        synchronized(children) {
            child.parent = null
            children.remove(child)
            changes.add(SmartChange(id, child, SmartChangeType.CHILDREN_REMOVE))
        }
    }

    fun fetchAndResetChanges(changes: MutableList<SmartChange> = mutableListOf()): List<SmartChange> {
        synchronized(children) {
            changes.addAll(this.changes)

            getDirtySmartProperties().forEach { smartProperty ->
                val changeValue = mapOf(
                        "n" to smartProperty.name,
                        "v" to smartProperty.value
                )
                changes.add(SmartChange(id, changeValue, SmartChangeType.PROPERTY_CHANGE))
            }

            this.changes.clear()
            resetSmartProperties()

            children.forEach { child ->
                child.fetchAndResetChanges(changes)
            }
        }
        return changes
    }
}