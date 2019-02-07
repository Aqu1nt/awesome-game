package ch.awesome.game.client.rendering.models

import ch.awesome.game.client.rendering.animation.Animation
import ch.awesome.game.client.rendering.animation.Skeleton

class AnimatedModel(val texturedModel: TexturedModel, val skeleton: Skeleton, val animations: MutableList<Animation>) {

    fun getAnimation(name: String): Animation? {
        for (animation in animations) {
            if (animation.name == name) {
                return animation
            }
        }

        return null
    }
}