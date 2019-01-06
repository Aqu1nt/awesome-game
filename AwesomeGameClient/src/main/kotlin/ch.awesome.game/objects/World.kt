package ch.awesome.game.objects

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.state.GameNode
import ch.awesome.game.state.interfaces.Renderable

class World: GameNode(id = "WORLD"), Renderable {

    override fun render(renderer: GameRenderer) {
        console.log(children.size)
    }
}