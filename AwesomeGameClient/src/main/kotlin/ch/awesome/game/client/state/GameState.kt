package ch.awesome.game.client.state

import ch.awesome.game.client.objects.Player
import ch.awesome.game.client.objects.base.Scene
import ch.awesome.game.client.rendering.Light
import ch.awesome.game.client.state.interfaces.LightSource
import ch.awesome.game.common.network.events.IGameStateNode
import ch.awesome.game.common.utils.ISmartChange
import ch.awesome.game.common.utils.SmartChangeType

class GameState(
        private val afterNodeCreate: (GameNode) -> Unit = {},
        private val afterNodeDestroy: (GameNode) -> Unit = {}
) {

    var playerId: String? = null
    val player: Player? get() = playerId?.let { playerId -> scene?.find(playerId) as Player? }
    var scene: Scene? = null
        private set

    private val factory = GameNodeFactory()

    fun calculateWorldMatrix() {
        scene?.calculateWorldMatrix()
    }

    fun getLightSources(): Array<Light> {
        return GameNode.allGameNodes().mapNotNull{
            if (it is LightSource) it.getLight()
            else null
        }.toTypedArray()
    }

    fun update(tpf: Float) {
        scene?.update(tpf)
    }

    fun replaceState(state: IGameStateNode) {
        scene = factory.createNode(state.data.asDynamic().type as String, state.data) as Scene
        afterNodeCreate(scene!!)

        fun addStateToNode(parent: GameNode, childState: IGameStateNode) {
            val gameNode = factory.createNode(childState.data.asDynamic().type as String, childState.data)
            parent.addChild(gameNode)
            afterNodeCreate(gameNode)

            for (child in childState.children) {
                addStateToNode(gameNode, child)
            }
        }

        for (childState in state.children) {
            addStateToNode(scene!!, childState)
        }
    }

    fun applyChanges(changes: List<ISmartChange>) {
        for (change in changes) {
            when (SmartChangeType.valueOf(change.type.toString())) {
                SmartChangeType.CHILDREN_ADD    -> {
                    val node = scene?.find(change.id)
                               ?: throw IllegalStateException("Cannot add child to non-existing node ${change.id}")
                    val initialState = change.value.asDynamic()
                    val type = initialState.type as String
                    val gameNode = factory.createNode(type, initialState)
                    if (node.find(gameNode.id) == null) {
                        node.addChild(gameNode)
                        afterNodeCreate(gameNode)
                    }
                }
                SmartChangeType.CHILDREN_REMOVE -> {
                    val node = scene?.find(change.id)
                               ?: throw IllegalStateException("Cannot remove child from non-existing node ${change.id}")
                    val childToRemove = node.find(change.value.asDynamic().id as String)
                                        ?: throw IllegalStateException("Cannot remove non-existing child ${change.value.asDynamic().id}")
                    node.removeChild(childToRemove)
                    afterNodeDestroy(childToRemove)
                }
                SmartChangeType.PROPERTY_CHANGE -> {
                    val node = scene?.find(change.id)
                               ?: throw IllegalStateException("Cannot update property of non-existing node ${change.id}")
                    node.state[change.value.asDynamic().n as String] = change.value.asDynamic().v
                }
                SmartChangeType.EVENT           -> {
                    val node = scene?.find(change.id)
                               ?: throw IllegalStateException("Cannot fire vent ${change.value} of non-existing node ${change.id}")
                    node.fireEvent(change.value as String)
                }
            }
        }
    }
}
