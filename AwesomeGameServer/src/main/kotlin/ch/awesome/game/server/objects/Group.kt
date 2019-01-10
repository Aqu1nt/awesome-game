package ch.awesome.game.server.objects

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.server.utils.withSmartProperties

class Group: MovingBaseObject() {

    init {
        withSmartProperties()
        addChild(Lamp().apply { position = Vector3f(-10.0f, 0.0f, 10.0f); color = Vector3f(1.0f, 0.9f, 0.4f) })
        addChild(Lamp().apply { position = Vector3f(10.0f, 0.0f, 10.0f); color = Vector3f(1.0f, 0.0f, 0.0f) })
        rotationVelocity = Vector3f(0f, 30f, 0f)
    }
}