package ch.awesome.game.client.objects

import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.interfaces.Renderable

class World(state: dynamic = null): GameNode(id = "WORLD", state = state), Renderable {

    override fun render(renderer: GameRenderer) {

    }
}
