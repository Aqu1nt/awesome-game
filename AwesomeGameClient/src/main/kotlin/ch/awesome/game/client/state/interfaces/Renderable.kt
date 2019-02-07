package ch.awesome.game.client.state.interfaces

import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.lib.WebGL2RenderingContext

interface Renderable {

    val animated: Boolean

    fun initModels(gl: WebGL2RenderingContext) { }
    fun render(gameRenderer: GameRenderer) { }
    fun renderParticles(gameRenderer: GameRenderer, camera: Camera) { }
}