package ch.awesome.game.client.objects

import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f

class LampLight(val lamp: Lamp): LightNode(), Renderable {

    lateinit var model: TexturedModel

    init {
        scale = Vector3f(3f, 3f, 3f)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = Texture(ModelCreator.loadTexture(gl, TextureImageType.CUBE), 1.0f, 20.0f)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.CUBE), texture)
    }

    override fun render(renderer: GameRenderer) {
        renderer.render(model, worldMatrix)
    }

    override fun getLightColor(): IVector3f {
        return lamp.color
    }
}


class Lamp(state: dynamic): MovingBaseObject(state), Renderable {

    val light = LampLight(this).apply {
        position = Vector3f(0f, 15f, 0f)
    }

    var color: IVector3f by StateProperty()

    lateinit var model: TexturedModel

    init {
        addChild(light)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = Texture(ModelCreator.loadTexture(gl, TextureImageType.LAMP), 1.0f, 20.0f)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.LAMP), texture)
        light.initModels(gl)
    }

    override fun render(renderer: GameRenderer) {
        renderer.render(model, worldMatrix)
    }
}