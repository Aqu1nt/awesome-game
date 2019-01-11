package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.state.interfaces.Renderer

interface ModelRenderer: Renderer {
    val model: TexturedModel?
}