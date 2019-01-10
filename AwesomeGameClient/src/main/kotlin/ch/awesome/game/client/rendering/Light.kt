package ch.awesome.game.client.rendering

import ch.awesome.game.common.math.Vector3f

class Light (val position: Vector3f,
             val color: Vector3f = Vector3f(1f, 1f, 1f),
             val attenuation: Vector3f = Vector3f(1.0f, 0.01f, 0.002f)) {
}