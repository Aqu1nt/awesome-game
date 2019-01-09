package ch.awesome.game.server.objects

import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.server.utils.withSmartProperties

class Armor(initName: String? = null): BaseObject() {
    var name: String? by SmartProperty(initName)

    init { withSmartProperties() }
}