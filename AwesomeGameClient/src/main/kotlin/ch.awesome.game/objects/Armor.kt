package ch.awesome.game.objects

import ch.awesome.game.state.GameNode
import ch.awesome.game.state.StateProperty

class Armor(state: dynamic): GameNode(state) {

    private var name by StateProperty<String>()
}