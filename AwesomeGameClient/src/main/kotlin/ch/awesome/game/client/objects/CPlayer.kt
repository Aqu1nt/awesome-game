package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CMovingBaseObject
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.SimpleModelData
import ch.awesome.game.client.rendering.loading.wavefront.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.IPlayer

class CPlayer(state: dynamic) : CMovingBaseObject(state), IPlayer<IVector3f>, Renderable {

    companion object {
        private val renderer = SimpleModelData(modelType = ModelType.PLAYER,
                textureImageType = TextureImageType.PLAYER,
                lightMapType = TextureImageType.PLAYER_LIGHTMAP)

        private val armorRenderer = SimpleModelData(modelType = ModelType.PLAYER_ARMOR,
                textureImageType = TextureImageType.PLAYER_ARMOR,
                reflectivity = 0.5f,
                shineDamper = 10.0f)
    }

    override fun update(tpf: Float) {
        super.update(tpf)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        renderer.initModels(gl)
        armorRenderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer, worldMatrix)
        armorRenderer.render(gameRenderer, worldMatrix)
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