package ch.awesome.game.client.objects

import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class Group(state: dynamic): MovingBaseObject(state), Renderable {

    lateinit var model: TexturedModel

    init {
        simulateRotation = true
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = Texture(ModelCreator.loadTexture(gl, TextureImageType.LAMP), 1.0f, 20.0f)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.LAMP), texture)
    }

    override fun render(renderer: GameRenderer) {
        renderer.render(model, worldMatrix.copy().apply { this.setScale(0.5f, 0.5f, 0.5f) })
    }
}