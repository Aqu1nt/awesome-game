package ch.awesome.game.server.utils

import ch.awesome.game.common.utils.SmartChange
import ch.awesome.game.common.utils.SmartChangeType
import java.util.*

@Suppress("UNCHECKED_CAST")
open class SmartTreeItem<T: SmartTreeItem<T>>(val id: String = UUID.randomUUID().toString()) {

    protected var parent: T? = null
    protected var changes: MutableList<SmartChange> = mutableListOf()
    protected var children: MutableList<T> = mutableListOf()

    fun addChild(child: T) {
        synchronized(children) {
            child.parent = this as T
            children.add(child)
            changes.add(SmartChange(id, child, SmartChangeType.CHILDREN_ADD))
        }
    }

    fun removeChild(child: T) {
        synchronized(children) {
            child.parent = null
            children.remove(child)
            changes.add(SmartChange(id, child, SmartChangeType.CHILDREN_REMOVE))
        }
    }

    fun children(): List<T> {
        return children
    }

    fun root(): T {
        return parent?.root() ?: this as T
    }

    fun fireEvent(event: String) {
        changes.add(SmartChange(id, event, SmartChangeType.EVENT))
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