package ch.awesome.game.client.state

import ch.awesome.game.client.objects.Bullet
import ch.awesome.game.client.objects.Lamp
import ch.awesome.game.client.objects.Player
import ch.awesome.game.client.objects.World
import ch.awesome.game.client.objects.base.Group
import ch.awesome.game.client.objects.base.MovingGroup

typealias GameNodeCreator = (state: dynamic) -> GameNode

class GameNodeFactory {

    private val creators: Map<String, GameNodeCreator> = mapOf(
            "World" to { state: dynamic -> World(state) },
            "Player" to { state: dynamic -> Player(state) },
            "Lamp" to { state: dynamic -> Lamp(state) },
            "Group" to { state: dynamic -> Group(state) },
            "MovingGroup" to { state: dynamic -> MovingGroup(state) },
            "Bullet" to { state: dynamic -> Bullet(state) }
    )

    fun createNode(type: String, state: dynamic): GameNode {
        val creator = creators[type] ?: throw IllegalStateException("No GameNode creator for type $type!")
        return creator(state)
    }
}