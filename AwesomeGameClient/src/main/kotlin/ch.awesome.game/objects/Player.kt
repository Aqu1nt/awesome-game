package ch.awesome.game.objects

import ch.awesome.game.engine.rendering.*
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.state.StateProperty
import ch.awesome.game.state.interfaces.Renderable
import ch.awesome.game.utils.Vector3f

class Player(state: dynamic): MovingBaseObject(state), Renderable {

    private var age by StateProperty<Int>()
    private var name by StateProperty<String>()

    lateinit var model: TexturedModel
    lateinit var lamp: TexturedModel

    var modelMatrix = Matrix4f()
    var lampModelMatrix = Matrix4f()

    var yaw = 0.0f

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = Texture(ModelCreator.loadTexture(gl, TextureImageType.BUNNY), 1.0f, 20.0f)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.BUNNY), texture)

        val lampTexture = Texture(ModelCreator.loadTexture(gl, TextureImageType.BUNNY), 1.0f, 20.0f)
        lamp = TexturedModel(OBJModelLoader.getModel(ModelType.LAMP), lampTexture)
    }

    override fun update(tpf: Float) {
        if (velocity.x > 0.0f) yaw = 90.0f
        if (velocity.x < 0.0f) yaw = -90.0f
        if (velocity.z > 0.0f) yaw = 0.0f
        if (velocity.z < 0.0f) yaw = 180.0f
        super.update(tpf)
    }

    override fun render(renderer: GameRenderer) {
        Matrix4f.modelMatrix(modelMatrix, localPosition, Vector3f(0.0f, yaw, 0.0f), Vector3f(0.5f, 0.5f, 0.5f))
        renderer.render(model, modelMatrix)

        Matrix4f.modelMatrix(lampModelMatrix, Vector3f(-10.0f, 0.0f, 10.0f), Vector3f(0.0f, 0.0f, 0.0f),
                Vector3f(1.0f, 1.0f, 1.0f))
        renderer.render(lamp, lampModelMatrix)

        Matrix4f.modelMatrix(lampModelMatrix, Vector3f(10.0f, 0.0f, 10.0f), Vector3f(0.0f, 0.0f, 0.0f),
                Vector3f(1.0f, 1.0f, 1.0f))
        renderer.render(lamp, lampModelMatrix)
    }
}