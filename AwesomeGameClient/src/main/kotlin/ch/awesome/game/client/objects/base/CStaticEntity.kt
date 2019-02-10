package ch.awesome.game.client.objects.base

import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.models.TexturedModel
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.common.math.Matrix4f

class CStaticEntity(state: dynamic): GameNode(state), Renderable {

    override var animated = false

    lateinit var model: TexturedModel
    var modelMatrix = Matrix4f().identity()

    override fun render(gameRenderer: GameRenderer) {
        gameRenderer.renderModel(model, matrix)
    }
}