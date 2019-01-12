package ch.awesome.game.client.objects.base

import ch.awesome.game.client.rendering.Light
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.interfaces.LightSource
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f

abstract class CLightNode(val color: IVector3f? = null,
                          val attenuation: IVector3f? = null): GameNode(), LightSource {

    companion object {
        private val DEFAULT_COLOR = Vector3f(1f, 1f, 1f)
        private val DEFAULT_ATTENUATION = Vector3f(1.0f, 0.01f, 0.002f)
    }

    private val light = Light(Vector3f(position))

    override fun getLight(): Light {
        light.position.set(getLightPosition())
        light.color.set(getLightColor())
        light.attenuation.set(getLightAttenuation())
        return light
    }

    open fun getLightPosition(): IVector3f {
        return worldTranslation
    }

    open fun getLightColor(): IVector3f {
        return color ?: DEFAULT_COLOR
    }

    open fun getLightAttenuation(): IVector3f {
        return attenuation ?: DEFAULT_ATTENUATION
    }
}