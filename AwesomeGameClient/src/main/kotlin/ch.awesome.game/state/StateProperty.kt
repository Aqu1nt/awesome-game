package ch.awesome.game.state

import kotlin.reflect.KProperty

class StateProperty<T> {

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        val thisRefNode = thisRef as GameNode
        return thisRefNode.state[prop.name] as T
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: T) {
        val thisRefNode = thisRef as GameNode
        thisRefNode.state[prop.name] = value
    }
}