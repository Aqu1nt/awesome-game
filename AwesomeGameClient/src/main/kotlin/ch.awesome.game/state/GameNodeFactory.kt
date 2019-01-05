package ch.awesome.game.state

import ch.awesome.game.objects.Armor
import ch.awesome.game.objects.Player
import ch.awesome.game.objects.World

typealias GameNodeCreator = (state: dynamic) -> GameNode

object GameNodeFactory {

    private val creators: Map<String, GameNodeCreator> = mapOf(
            "World" to { _: dynamic -> World() },
            "Player" to ::Player,
            "Armor" to ::Armor
    )

    fun createNode(type: String, state: dynamic): GameNode {
        val creator = creators[type] ?: throw IllegalStateException("No GameNode creator for type $type!")
        return creator(state)
    }
}