package ch.awesome.game.client.state.interfaces

import ch.awesome.game.client.rendering.Camera
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

interface Renderable {

    fun initModels(gl: WebGL2RenderingContext) { }
    fun render(gameRenderer: GameRenderer) { }
    fun renderParticles(gameRenderer: GameRenderer, camera: Camera) { }
}