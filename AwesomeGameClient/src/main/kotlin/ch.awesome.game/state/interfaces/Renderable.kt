package ch.awesome.game.state.interfaces

import ch.awesome.game.engine.rendering.GameRenderer

interface Renderable {
    fun render(renderer: GameRenderer)
}