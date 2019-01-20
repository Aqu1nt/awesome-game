package ch.awesome.game.client.state

import ch.awesome.game.client.GameClient
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Quaternion
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.base.IBaseObject
import kotlin.random.Random

// Placeholder class to indicate a state not from server
class ClientOnlyState

fun randomId(): String {
    return Random.nextDouble().toString().substring(2, 15)
}

open class GameNode(val state: dynamic = ClientOnlyState(),
                    override val id: String = state.id as String? ?: randomId()) : IBaseObject<IVector3f> {

    val game: GameClient
        get() = GameClient.instance

    companion object {
        private val allNodes = mutableMapOf<String, GameNode>()

        fun allNodes(): Map<String, GameNode> {
            return allNodes
        }

        fun allGameNodes(): Collection<GameNode> {
            return allNodes.values
        }
    }

    final override var position: IVector3f by StateProperty()
    final override var scale: IVector3f by StateProperty()
    final override var rotation: IVector3f by StateProperty()

    var matrix: Matrix4f = Matrix4f().identity()
        private set
        get() {
            field.identity()
            field.translate(actualPosition())
            field.rotate(actualRotation())
            field.scale(actualScale())
            return field
        }

    val worldScale = Vector3f()
    val worldRotation = Quaternion()
    val worldTranslation = Vector3f()
    val worldMatrix = Matrix4f().identity()

    protected val children = mutableListOf<GameNode>()

    var parent: GameNode? = null
        private set

    init {
        if (state is ClientOnlyState) {
            position = Vector3f()
            rotation = Vector3f()
            scale = Vector3f(1f, 1f, 1f)
        }
    }

    fun getChildren(): List<GameNode> {
        return children
    }

    fun calculateWorldMatrix() {
        val finalParent = parent

        worldScale.set(actualScale())
        worldTranslation.set(actualPosition())
        worldRotation.fromAngles(actualRotation()).inverseLocal()

        if (finalParent != null) {
            worldScale.multLocal(finalParent.worldScale)
            finalParent.worldRotation.mult(worldRotation, worldRotation)
            worldTranslation.multLocal(finalParent.worldScale)
            finalParent.worldRotation
                    .multLocal(worldTranslation)
                    .addLocal(finalParent.worldTranslation)
        }

        worldMatrix.identity()
        worldMatrix.translate(worldTranslation)
        worldRotation.inverse().toRotationMatrix(worldMatrix)
        worldMatrix.scale(worldScale)

        for (child in children) {
            child.calculateWorldMatrix()
        }
    }

    open fun actualPosition(): IVector3f {
        return position
    }

    open fun actualRotation(): IVector3f {
        return rotation
    }

    open fun actualScale(): IVector3f {
        return scale
    }

    open fun fireEvent(event: String) {

    }

    open fun afterAdd() {

    }

    open fun afterRemove() {

    }

    fun addChild(child: GameNode) {
        children.add(child)
        child.parent = this
        allNodes[child.id] = child
        child.afterAdd()
    }

    fun removeChild(child: GameNode) {
        children.remove(child)
        child.parent = null
        for (node in child.descendants()) {
            allNodes.remove(node.id)
            node.afterRemove()
        }
        child.afterRemove()
    }

    fun descendants(list: MutableList<GameNode> = mutableListOf()): List<GameNode> {
        list.add(this)
        for (child in children) {
            child.descendants(list)
        }
        return list
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

    open fun beforeUpdate() {
        for (child in children) {
            child.beforeUpdate()
        }
    }

    open fun update(tpf: Float) {
        for (child in children) {
            child.update(tpf)
        }
    }

    open fun afterUpdate() {
        for (child in children) {
            child.afterUpdate()
        }
    }
}
