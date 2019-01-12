package ch.awesome.game.client.rendering.renderer

import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.Texture
import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.OBJModelLoader
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.webgl2.WebGL2RenderingContext

class SimpleModelData(private val gameNode: GameNode,
                      private val modelType: ModelType,
                      private val textureImageType: TextureImageType,
                      private val reflectivity: Float = 1f,
                      private val shineDamper: Float = 20f,
                      private val lightMapType: TextureImageType?= null) {

    var model: TexturedModel? = null
        private set

    fun initModels(gl: WebGL2RenderingContext) {
        val lightMap = lightMapType ?.let { ModelCreator.loadTexture(gl, lightMapType) }
        val texture = Texture(ModelCreator.loadTexture(gl, textureImageType), reflectivity, shineDamper, lightMap)
        model = TexturedModel(OBJModelLoader.getModel(modelType), texture)
    }

    fun render(gameRenderer: GameRenderer) {
        val localModel = model
        if (localModel != null) {
            gameRenderer.renderModel(localModel, gameNode.worldMatrix)
        }
        else {
            throw IllegalStateException("Cannot renderModel null model!")
        }
    }
}