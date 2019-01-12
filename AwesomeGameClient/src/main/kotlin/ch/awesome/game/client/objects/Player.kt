package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.MovingBaseObject
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.SimpleModelData
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.IPlayer

class Player(state: dynamic) : MovingBaseObject(state), IPlayer<IVector3f>, Renderable {

    private val renderer = SimpleModelData(gameNode = this,
                                               modelType = ModelType.PLAYER,
                                               textureImageType = TextureImageType.PLAYER,
                                               lightMapType = TextureImageType.PLAYER_LIGHTMAP)

    override fun update(tpf: Float) {
        if (velocity.x > 0.0f) actualRotation().y = -90.0f
        if (velocity.x < 0.0f) actualRotation().y = 90.0f
        if (velocity.z > 0.0f) actualRotation().y = 0.0f
        if (velocity.z < 0.0f) actualRotation().y = 180.0f
        super.update(tpf)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer)
    }
}