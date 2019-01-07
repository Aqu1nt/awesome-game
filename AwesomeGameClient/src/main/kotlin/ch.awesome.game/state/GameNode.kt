package ch.awesome.game.state

import ch.awesome.game.objects.IBaseObject

open class GameNode(val state: dynamic = null,
                    override val id: String = state.id as String) : IBaseObject {

    companion object {
        private val allNodes = mutableMapOf<String, GameNode>()

        fun allGameNodes(): Collection<GameNode> {
            return allNodes.values
        }
    }

    protected val children = mutableListOf<GameNode>()

    var parent: GameNode? = null
        private set

    fun fireEvent(event: String) {

    }

    fun addChild(child: GameNode) {
        children.add(child)
        child.parent = this
        allNodes[child.id] = child
    }

    fun removeChild(child: GameNode) {
        children.remove(child)
        child.parent = null
        allNodes.remove(child.id)
    }

    fun find(id: String): GameNode? {
        if (this.id == id) {
            return this
        }
        val nodeFromCache = allNodes[id]
        if (nodeFromCache != null) {
            return nodeFromCache
        }
        children.forEach { child ->
            val node = child.find(id)
            if (node != null) {
                return node
            }
        }
        return null
    }

    open fun update(tpf: Float) {
        for (child in children) {
            child.update(tpf)
        }
    }
}
