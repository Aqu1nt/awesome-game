package ch.awesome.game.client.rendering.animation

class Animation(val name: String, val keyFrames: MutableList<AnimationKeyFrame>) {

    val length: Float
        get() {
            return keyFrames[keyFrames.size - 1].time
        }
}