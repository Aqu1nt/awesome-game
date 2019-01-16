package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CMovingBaseObject
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.loading.wavefront.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.SimpleModelData
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class CBullet(state: dynamic) : CMovingBaseObject(state), Renderable {

    companion object {
        private val renderer = SimpleModelData(ModelType.BULLET, TextureImageType.BULLET)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer, worldMatrix)
    }


}