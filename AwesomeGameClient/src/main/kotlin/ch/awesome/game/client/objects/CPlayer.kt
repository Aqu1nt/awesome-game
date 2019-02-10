package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CMovingBaseObject
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.rendering.SimpleModelData
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.AnimatedModelData
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.animation.Animator
import ch.awesome.game.client.rendering.loading.ModelLoader
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.models.AnimatedModel
import ch.awesome.game.client.rendering.textures.Texture
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.IPlayer

class CPlayer(state: dynamic) : CMovingBaseObject(state), IPlayer<IVector3f>, Renderable {

    var health: Float by StateProperty()
    var level: Float by StateProperty()
    var animation: String by StateProperty()

    private lateinit var animatorLV1: Animator
    private lateinit var animatorLV3: Animator

    override var animated = true


    companion object {

        private val rendererLV2 = SimpleModelData(modelType = ModelType.PLAYER_LV2, textureImageType = TextureImageType.PLAYER_LV2)

        private val armorRenderer = SimpleModelData(modelType = ModelType.PLAYER_ARMOR,
                textureImageType = TextureImageType.PLAYER_ARMOR,
                reflectivity = 0.5f,
                shineDamper = 10.0f)
    }

    private val rendererLV1 = AnimatedModelData(modelType = ModelType.PLAYER,
            textureImageType = TextureImageType.PLAYER,
            lightMapType = TextureImageType.PLAYER_LIGHTMAP)

    private val rendererLV3 = AnimatedModelData(modelType = ModelType.PLAYER_LV3,
            textureImageType = TextureImageType.PLAYER_LV3,
            reflectivity = 0.0f,
            shineDamper = 0.0f)

    override fun update(tpf: Float) {
        if (level == 0.0f) {
            animatorLV1.setAnimation(animation)
            animatorLV1.update(tpf)
        } else if (level == 2.0f) {
            animatorLV3.setAnimation(animation)
            animatorLV3.update(tpf)
        }
        super.update(tpf)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        rendererLV1.initModels(gl)
        rendererLV2.initModels(gl)
        rendererLV3.initModels(gl)
        armorRenderer.initModels(gl)

        animatorLV1 = Animator(rendererLV1.model!!)
        animatorLV3 = Animator(rendererLV3.model!!)
    }

    override fun render(gameRenderer: GameRenderer) {
        if (health <= 0.0f) return

        animated = level == 0.0f || level == 2.0f

        when (level) {
            0.0f -> rendererLV1.render(gameRenderer, worldMatrix)
            1.0f -> rendererLV2.render(gameRenderer, worldMatrix)
            2.0f -> rendererLV3.render(gameRenderer, worldMatrix)
        }
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