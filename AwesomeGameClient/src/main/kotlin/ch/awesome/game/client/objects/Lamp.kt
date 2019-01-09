package ch.awesome.game.client.objects

import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.LightSource
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.utils.IVector3f
import ch.awesome.game.common.utils.Vector3f

class Lamp(state: dynamic): GameNode(state), Renderable, LightSource {

    var color: IVector3f by StateProperty()

    lateinit var model: TexturedModel
    var modelMatrix = Matrix4f()

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = Texture(ModelCreator.loadTexture(gl, TextureImageType.BUNNY), 1.0f, 20.0f)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.LAMP), texture)
    }

    override fun render(renderer: GameRenderer) {
        Matrix4f.modelMatrix(modelMatrix, position, rotation, scale)
        renderer.render(model, modelMatrix)
    }

    override fun getLight(): Light {
        return Light(Vector3f(position.x, position.y + 7.0f, position.z), Vector3f(color), Vector3f(1.0f, 0.01f, 0.002f))
    }
}