package ch.awesome.game.client.objects

import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.interfaces.Renderable

class World: GameNode(id = "WORLD"), Renderable {

    override fun render(renderer: GameRenderer) {

    }
}
