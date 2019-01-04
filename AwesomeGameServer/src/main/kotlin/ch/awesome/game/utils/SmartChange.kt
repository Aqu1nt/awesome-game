package ch.awesome.game.utils

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

private val smartChangeObjectMapper = jacksonObjectMapper()

data class SmartChange(
        @field:JsonIgnore
        val target: SmartChangeTarget,
        val value: Any?,
        val type: SmartChangeType) {

    @JsonProperty
    fun getId(): String {
        return target.id
    }

    fun toJSON(): ByteArray {
        return smartChangeObjectMapper.writeValueAsBytes(this)
    }
}

interface SmartChangeTarget {
    val id: String

    @JsonProperty("class")
    fun getClass(): String {
        return javaClass.simpleName
    }
}

fun Collection<SmartChange>.toJSON(): ByteArray {
    return smartChangeObjectMapper.writeValueAsBytes(this)
}