package ch.awesome.game.client.state

import ch.awesome.game.client.objects.CBullet
import ch.awesome.game.client.objects.CLamp
import ch.awesome.game.client.objects.CPlayer
import ch.awesome.game.client.objects.CWorld
import ch.awesome.game.client.objects.base.CGroup
import ch.awesome.game.client.objects.base.CMovingGroup

typealias GameNodeCreator = (state: dynamic) -> GameNode

class GameNodeFactory {

    private val creators: Map<String, GameNodeCreator> = mapOf(
            "SWorld" to { state: dynamic -> CWorld(state) },
            "SPlayer" to { state: dynamic -> CPlayer(state) },
            "SLamp" to { state: dynamic -> CLamp(state) },
            "SGroup" to { state: dynamic -> CGroup(state) },
            "SMovingGroup" to { state: dynamic -> CMovingGroup(state) },
            "SBullet" to { state: dynamic -> CBullet(state) }
    )

    fun createNode(type: String, state: dynamic): GameNode {
        val creator = creators[type] ?: throw IllegalStateException("No GameNode creator for type $type!")
        return creator(state)
    }
}