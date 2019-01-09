package ch.awesome.game.client.objects

import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty

class Armor(state: dynamic): GameNode(state) {

    private var name by StateProperty<String>()
}