package ch.awesome.game.objects

import ch.awesome.game.network.GameWebSocketHandler
import ch.awesome.game.network.NetworkEvent
import ch.awesome.game.utils.SmartProperty
import ch.awesome.game.utils.Vector3f
import ch.awesome.game.utils.withSmartProperties
import com.fasterxml.jackson.annotation.JsonIgnore

class Player(initName: String? = null, initAge: Int? = null): BaseObject() {

    var name: String? by SmartProperty(initName)
    var age: Int? by SmartProperty(initAge)
    var position: Vector3f by SmartProperty(Vector3f())
    var direction: Vector3f by SmartProperty(Vector3f())

    @JsonIgnore
    var webSocketHandler: GameWebSocketHandler? = null

    init { withSmartProperties() }

    fun sendEvent(event: NetworkEvent<*>) {
        webSocketHandler?.sendEvent(event)
    }

    override fun update(tpf: Float) {
        position += Vector3f(direction.x * tpf * 10f, direction.y * tpf * 10f, direction.z * tpf * 10f)
        super.update(tpf)
    }
}