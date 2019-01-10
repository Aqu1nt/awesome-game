package ch.awesome.game.client.state

import ch.awesome.game.client.objects.*

typealias GameNodeCreator = (state: dynamic) -> GameNode

class GameNodeFactory {

    private val creators: Map<String, GameNodeCreator> = mapOf(
            "World" to ::World,
            "Player" to ::Player,
            "Armor" to ::Armor,
            "Lamp" to ::Lamp,
            "Group" to ::Group
    )

    fun createNode(type: String, state: dynamic): GameNode {
        val creator = creators[type] ?: throw IllegalStateException("No GameNode creator for type $type!")
        return creator(state)
    }
}