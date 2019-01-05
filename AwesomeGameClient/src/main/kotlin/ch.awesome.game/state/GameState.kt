package ch.awesome.game.state

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.objects.World
import ch.awesome.game.state.interfaces.Renderable
import ch.awesome.game.utils.ISmartChange
import ch.awesome.game.utils.SmartChangeType

object GameState: Renderable {

    private val world = World()

    override fun render(renderer: GameRenderer) {
        for (gameNode in GameNode.allGameNodes()) {
            if (gameNode is Renderable) {
                gameNode.render(renderer)
            }
        }
    }

    fun applyChanges(changes: List<ISmartChange>) {
        for (change in changes) {
            when (SmartChangeType.valueOf(change.type.toString())) {
                SmartChangeType.CHILDREN_ADD    -> {
                    val node = world.find(change.id)
                               ?: throw IllegalStateException("Cannot add child to non-existing node ${change.id}")
                    val initialState = change.value.asDynamic()
                    val type = initialState.type as String
                    val gameNode = GameNodeFactory.createNode(type, initialState)
                    node.addChild(gameNode)
                }
                SmartChangeType.CHILDREN_REMOVE -> {
                    val node = world.find(change.id)
                               ?: throw IllegalStateException("Cannot remove child from non-existing node ${change.id}")
                    val childToRemove = node.find(change.value.asDynamic().id as String)
                                        ?: throw IllegalStateException("Cannot remove non-existing child ${change.value.asDynamic().id}")
                    node.removeChild(childToRemove)
                }
                SmartChangeType.PROPERTY_CHANGE -> {
                    val node = world.find(change.id)
                               ?: throw IllegalStateException("Cannot update property of non-existing node ${change.id}")
                    node.state[change.value.asDynamic().n as String] = change.value.asDynamic().v
                }
                SmartChangeType.EVENT           -> {
                    val node = world.find(change.id)
                               ?: throw IllegalStateException("Cannot fire vent ${change.value} of non-existing node ${change.id}")
                    node.fireEvent(change.value as String)
                }
            }
        }
    }
}
