package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.MovingBaseObject
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.SimpleModelRenderer
import ch.awesome.game.client.state.interfaces.Renderer
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.IPlayer

class Player(state: dynamic) : MovingBaseObject(state), IPlayer<IVector3f>, Renderer {

    private val modelMatrix = Matrix4f()
    private val renderer = SimpleModelRenderer(this, ModelType.PLAYER, TextureImageType.PLAYER)
    private var yaw = 0.0f


    override fun update(tpf: Float) {
        if (velocity.x > 0.0f) yaw = 90.0f
        if (velocity.x < 0.0f) yaw = -90.0f
        if (velocity.z > 0.0f) yaw = 0.0f
        if (velocity.z < 0.0f) yaw = 180.0f
        super.update(tpf)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        Matrix4f.modelMatrix(modelMatrix, worldTranslation, Vector3f(0.0f, yaw, 0.0f), Vector3f(1.0f, 1.0f, 1.0f))
        gameRenderer.render(this.renderer.model!!, modelMatrix)
    }
}