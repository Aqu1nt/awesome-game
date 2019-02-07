package ch.awesome.game.client.rendering.models

import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.animation.Animation
import ch.awesome.game.client.rendering.animation.Skeleton
import ch.awesome.game.client.rendering.textures.Texture

class ModelData(val vertices: Array<Float>, val textureCoords: Array<Float>, val normals: Array<Float>, val indices: Array<Int>,
                val jointIDs: Array<Int>? = null, val weights: Array<Float>? = null, val skeleton: Skeleton? = null,
                val animations: MutableList<Animation>? = null) {

    fun createStaticModel(gl: WebGL2RenderingContext): Model {
        return ModelCreator.loadModel(gl, vertices, textureCoords, normals, indices)
    }

    fun createTexturedModel(gl: WebGL2RenderingContext, texture: Texture): TexturedModel {
        return TexturedModel(createStaticModel(gl), texture)
    }

    fun createAnimatedModel(gl: WebGL2RenderingContext, texture: Texture): AnimatedModel {
        val rawModel = ModelCreator.loadAnimatedModel(gl, vertices, textureCoords, normals, indices, jointIDs!!, weights!!)
        return AnimatedModel(TexturedModel(rawModel, texture), skeleton!!, animations!!)
    }
}