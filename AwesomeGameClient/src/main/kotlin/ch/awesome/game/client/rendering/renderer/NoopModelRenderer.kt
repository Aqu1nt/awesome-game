package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class NoopModelRenderer: ModelRenderer {

    override val model: TexturedModel?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun initModels(gl: WebGL2RenderingContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(gameRenderer: GameRenderer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}