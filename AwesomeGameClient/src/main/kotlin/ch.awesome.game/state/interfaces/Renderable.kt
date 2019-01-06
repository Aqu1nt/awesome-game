package ch.awesome.game.state.interfaces

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext

interface Renderable {

    fun initModels(gl: WebGL2RenderingContext) { }
    fun render(renderer: GameRenderer)
}