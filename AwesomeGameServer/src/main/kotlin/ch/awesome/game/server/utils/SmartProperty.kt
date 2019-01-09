package ch.awesome.game.server.utils

import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible

/**
 * Constant returned when no smart properties are available
 */
private val NO_SMART_PROPERTIES = emptySet<SmartProperty<*>>()

/**
 *
 */
val smartProperties = WeakHashMap<Any, MutableSet<SmartProperty<*>>>()

/**
 *
 */
class SmartProperty<T>(initialValue: T,
                       val set: ((T) -> T)? = null,
                       val get: (() -> T)? = null) {

    var value: T = initialValue
        private set

    var thisRef: Any? = null
        private set

    var dirty = false
        private set

    var name: String? = null
        private set

    private var registered = false

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        if (name == null) {
            name = prop.name
        }
        if (thisRef != null && !registered) {
            register(thisRef)
        }
        return value
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: T) {
        if (name == null) {
            name = prop.name
        }
        if (thisRef != null && !registered) {
            register(thisRef)
        }
        if (value != this.value) {
            dirty = true
        }

        this.value = set?.invoke(value) ?: value
    }

    private fun register(thisRef: Any) {
        this.thisRef = thisRef
        val set = smartProperties.computeIfAbsent(thisRef) {
            mutableSetOf()
        }
        set.add(this)
    }

    fun resetSmartProperty() {
        dirty = false
    }
}

/**
 * Returns all smart properties on an object
 */
fun Any.getSmartProperties(): Set<SmartProperty<*>> {
    return smartProperties[this] ?: NO_SMART_PROPERTIES
}

/**
 * Returns all smart properties that have been changed since the last time
 */
fun Any.getDirtySmartProperties(): Sequence<SmartProperty<*>> {
    return getSmartProperties().asSequence().filter { it.dirty }
}

/**
 * Resets all smart properties of this object
 */
fun Any.resetSmartProperties() {
    getSmartProperties().forEach { it.resetSmartProperty() }
}

/**
 * Force-Initializes all smart properties by accessing all getters once
 */
fun <T : Any> T.withSmartProperties(): T {
    this::class.members.forEach { member ->
        if (member is KMutableProperty<*>) {
            member.isAccessible = true
            member.getter.call(this)
        }
    }
    return this
}