package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.LightNode
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.SimpleModelRenderer
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderer
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.ILamp

class Lamp(state: dynamic) : GameNode(state), ILamp<IVector3f>, Renderer {

    class LampLight(val lamp: Lamp) : LightNode(attenuation = Vector3f(1f, 0.01f, 0.00001f)) {
        override fun getLightColor(): IVector3f {
            return lamp.color
        }
    }

    override var color: IVector3f by StateProperty()

    private val light = LampLight(this).apply { position = Vector3f(0f, 14f, 0f) }
    private val renderer = SimpleModelRenderer(this, ModelType.LAMP, TextureImageType.LAMP)

    init {
        addChild(light)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer)
    }
}