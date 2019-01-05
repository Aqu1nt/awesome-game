package ch.awesome.game.objects

import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.withSmartProperties

class Armor(initName: String? = null): BaseObject() {
    var name: String? by SmartProperty(initName)

    init { withSmartProperties() }
}