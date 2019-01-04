package ch.awesome.game.objects

import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.SmartTreeItem
import ch.awesome.game.utils.withSmartProperties

class Armor(initName: String? = null): SmartTreeItem() {
    var name: String? by SmartProperty(initName)

    init { withSmartProperties() }
}