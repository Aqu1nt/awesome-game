package ch.awesome.game.objects

import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.SmartTreeItem
import ch.awesome.game.utils.Vector3f
import ch.awesome.game.utils.withSmartProperties

class Player(initName: String? = null, initAge: Int? = null): SmartTreeItem() {
    var name: String? by SmartProperty(initName)
    var age: Int? by SmartProperty(initAge)
    var position: Vector3f by SmartProperty(Vector3f())

    init { withSmartProperties() }
}