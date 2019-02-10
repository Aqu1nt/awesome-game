package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CMovingBaseObject
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.rendering.loading.wavefront.OBJModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.SimpleModelData
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.loading.ModelType

class CBullet(state: dynamic) : CMovingBaseObject(state), Renderable {

    override var animated = false

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