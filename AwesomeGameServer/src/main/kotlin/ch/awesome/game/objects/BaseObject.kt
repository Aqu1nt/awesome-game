package ch.awesome.game.objects

import ch.awesome.game.instance.Updateable
import ch.awesome.game.utils.SmartTreeItem
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

open class BaseObject(id: String = UUID.randomUUID().toString()): SmartTreeItem(id), Updateable, IBaseObject {

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