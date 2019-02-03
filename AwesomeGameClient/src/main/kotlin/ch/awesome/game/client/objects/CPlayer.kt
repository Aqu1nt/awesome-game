package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CMovingBaseObject
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.rendering.SimpleModelData
import ch.awesome.game.client.rendering.loading.wavefront.OBJModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.rendering.loading.ModelLoader
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.textures.Texture
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.IPlayer
import kotlin.browser.window

class CPlayer(state: dynamic) : CMovingBaseObject(state), IPlayer<IVector3f>, Renderable {

    var health: Float by StateProperty()
    var level: Float by StateProperty()

    companion object {
        private val rendererLV1 = SimpleModelData(modelType = ModelType.PLAYER,
                textureImageType = TextureImageType.PLAYER,
                lightMapType = TextureImageType.PLAYER_LIGHTMAP)

        private val rendererLV2 = SimpleModelData(modelType = ModelType.JELLYFISH,
                textureImageType = TextureImageType.JELLYFISH)

        private val armorRenderer = SimpleModelData(modelType = ModelType.PLAYER_ARMOR,
                textureImageType = TextureImageType.PLAYER_ARMOR,
                reflectivity = 0.5f,
                shineDamper = 10.0f)
    }

    override fun update(tpf: Float) {
        super.update(tpf)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        rendererLV1.initModels(gl)
        rendererLV2.initModels(gl)
        armorRenderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        if (health <= 0.0f) return

        if (level == 0.0f) {
            rendererLV1.render(gameRenderer, worldMatrix)
        }
        else if (level == 1.0f) {
            rendererLV2.render(gameRenderer, worldMatrix)
        }
//        armorRenderer.render(gameRenderer, worldMatrix)
    }

    override fun afterAdd() {
//        val physicsBody = BtRigidBody(1f,
//                BtDefaultMotionState(BtTransform()),
//                BtBoxShape(BtVector3f(0.75f, 0.75f, 1f))
//        )
//        physicsBody.userPointer = this
//        game.physics.dynamicsWorld.addRigidBody(physicsBody)
        super.afterAdd()
    }
}