package ch.awesome.game.objects

import ch.awesome.game.utils.SmartTreeItem
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

open class BaseObject(id: String = UUID.randomUUID().toString()): SmartTreeItem(id) {

    @JsonProperty("type")
    fun getType(): String {
        return javaClass.simpleName
    }
}