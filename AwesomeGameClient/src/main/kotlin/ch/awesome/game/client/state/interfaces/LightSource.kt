package ch.awesome.game.client.state.interfaces

import ch.awesome.game.client.rendering.Light

interface LightSource {

    fun getLight(): Light
}