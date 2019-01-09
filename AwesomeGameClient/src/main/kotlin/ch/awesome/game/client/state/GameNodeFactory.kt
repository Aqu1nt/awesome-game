package ch.awesome.game.client.state

import ch.awesome.game.client.objects.Armor
import ch.awesome.game.client.objects.Lamp
import ch.awesome.game.client.objects.Player
import ch.awesome.game.client.objects.World

typealias GameNodeCreator = (state: dynamic) -> GameNode

class GameNodeFactory {

    private val creators: Map<String, GameNodeCreator> = mapOf(
            "World" to { _: dynamic -> World() },
            "Player" to ::Player,
            "Armor" to ::Armor,
            "Lamp" to ::Lamp
    )

    fun createNode(type: String, state: dynamic): GameNode {
        val creator = creators[type] ?: throw IllegalStateException("No GameNode creator for type $type!")
        return creator(state)
    }
}