package ch.awesome.game.objects

import ch.awesome.game.instance.Updateable
import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.SmartTreeItem
import ch.awesome.game.utils.Vector3f
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

open class BaseObject(id: String = UUID.randomUUID().toString()): SmartTreeItem(id), Updateable, IBaseObject<Vector3f> {

    override var position: Vector3f by SmartProperty(Vector3f())
    override var scale: Vector3f by SmartProperty(Vector3f())
    override var rotation: Vector3f by SmartProperty(Vector3f())

    val worldPosition: Vector3f
        get() {
            val parentPosition =  parent?.let { parent ->
                if (parent is BaseObject) {
                    parent.worldPosition
                }
                else {
                    Vector3f()
                }
            }
            return position + (parentPosition ?: Vector3f())
        }

    @JsonProperty("type")
    fun getType(): String {
        return javaClass.simpleName
    }

    override fun beforeUpdate() {
        for (child in children) {
            if (child is Updateable) {
                child.beforeUpdate()
            }
        }
    }

    override fun update(tpf: Float) {
        for (child in children) {
            if (child is Updateable) {
                child.update(tpf)
            }
        }
    }

    override fun afterUpdate() {
        for (child in children) {
            if (child is Updateable) {
                child.afterUpdate()
            }
        }
    }
}