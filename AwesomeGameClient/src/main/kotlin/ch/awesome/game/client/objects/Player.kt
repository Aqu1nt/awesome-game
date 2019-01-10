package ch.awesome.game.client.objects

import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector3f

class Player(state: dynamic): MovingBaseObject(state), Renderable {

    private var age by StateProperty<Int>()
    private var name by StateProperty<String>()

    lateinit var model: TexturedModel

    val modelMatrix = Matrix4f()

    var yaw = 0.0f

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = Texture(ModelCreator.loadTexture(gl, TextureImageType.PLAYER), 1.0f, 20.0f)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.PLAYER), texture)
    }

    override fun update(tpf: Float) {
        if (velocity.x > 0.0f) yaw = 90.0f
        if (velocity.x < 0.0f) yaw = -90.0f
        if (velocity.z > 0.0f) yaw = 0.0f
        if (velocity.z < 0.0f) yaw = 180.0f
        super.update(tpf)
    }

    override fun render(renderer: GameRenderer) {
        Matrix4f.modelMatrix(modelMatrix, worldTranslation, Vector3f(0.0f, yaw, 0.0f), Vector3f(1.0f, 1.0f, 1.0f))
        renderer.render(model, modelMatrix)
    }
}