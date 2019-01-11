package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.MovingBaseObject
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.SimpleModelRenderer
import ch.awesome.game.client.state.interfaces.Renderer
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.IPlayer

class Player(state: dynamic) : MovingBaseObject(state), IPlayer<IVector3f>, Renderer {

    private val renderer = SimpleModelRenderer(this, ModelType.PLAYER, TextureImageType.PLAYER)

    override fun update(tpf: Float) {
        if (velocity.x > 0.0f) actualRotation().y = -90.0f
        if (velocity.x < 0.0f) actualRotation().y = 90.0f
        if (velocity.z > 0.0f) actualRotation().y = 0.0f
        if (velocity.z < 0.0f) actualRotation().y = 180.0f
//        calculateWorldMatrix()
        super.update(tpf)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer)
    }
}