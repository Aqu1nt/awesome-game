package ch.awesome.game.client.rendering

import ch.awesome.game.client.rendering.loading.wavefront.OBJModelType
import ch.awesome.game.client.rendering.loading.wavefront.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.loading.ModelLoader
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.textures.Texture
import ch.awesome.game.common.math.Matrix4f

class SimpleModelData(private val modelType: ModelType,
                      private val textureImageType: TextureImageType,
                      private val reflectivity: Float = 1f,
                      private val shineDamper: Float = 20f,
                      private val lightMapType: TextureImageType?= null) {

    var model: TexturedModel? = null
        private set

    fun initModels(gl: WebGL2RenderingContext) {
        if(model != null) return

        val lightMap = lightMapType ?.let { ModelCreator.loadTexture(gl, lightMapType) }
        val texture = Texture(ModelCreator.loadTexture(gl, textureImageType), reflectivity, shineDamper, lightMap)
        model = TexturedModel(ModelLoader.getModel(modelType), texture)
    }

    fun render(gameRenderer: GameRenderer, modelMatrix: Matrix4f) {
        val localModel = model
        if (localModel != null) {
            gameRenderer.renderModel(localModel, modelMatrix)
        }
        else {
            throw IllegalStateException("Cannot renderModel null model!")
        }
    }
}