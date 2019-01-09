package ch.awesome.game.state.interfaces

import ch.awesome.game.engine.rendering.Light

interface LightSource {

    fun getLight(): Light
}