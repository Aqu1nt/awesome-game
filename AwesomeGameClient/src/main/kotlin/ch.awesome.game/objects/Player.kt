package ch.awesome.game.objects

import ch.awesome.game.engine.rendering.*
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.state.StateProperty
import ch.awesome.game.state.interfaces.Renderable

class Player(state: dynamic): MovingBaseObject(state), Renderable {

    private var age by StateProperty<Int>()
    private var name by StateProperty<String>()

    lateinit var model: TexturedModel
    lateinit var grass: TexturedModel

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = ModelCreator.loadTexture(gl, TextureImageType.BOULDER)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.BOULDER), texture)
    }

    override fun render(renderer: GameRenderer) {
        renderer.render(model, localPosition.x, localPosition.y, localPosition.z)
    }
}