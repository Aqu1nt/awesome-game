package ch.awesome.game.objects

import ch.awesome.game.engine.rendering.GameRenderer
import ch.awesome.game.state.GameNode
import ch.awesome.game.state.StateProperty
import ch.awesome.game.state.interfaces.Renderable

class Player(state: dynamic): GameNode(state), Renderable {
    private var age by StateProperty<Int>()
    private var name by StateProperty<String>()
    private var position by StateProperty<dynamic>()

    override fun render(renderer: GameRenderer) {
        console.log("RENDERING", this)
        renderer.render(position.x as Float, position.y as Float, position.z as Float)
        console.log(position)
    }
}