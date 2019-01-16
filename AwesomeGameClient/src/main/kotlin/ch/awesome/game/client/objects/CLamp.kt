package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.CLightNode
import ch.awesome.game.client.rendering.*
import ch.awesome.game.client.rendering.loading.wavefront.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.math.Vector4f
import ch.awesome.game.common.objects.ILamp

class CLamp(state: dynamic) : GameNode(state), ILamp<IVector3f>, Renderable {

    class LampLight(private val lamp: CLamp) : CLightNode(attenuation = Vector3f(1f, 0.01f, 0.00001f)), Renderable {

        private var lightMatrix = Matrix4f().set(worldMatrix)
        private lateinit var particleModel: TexturedModel

        override fun getLightColor(): IVector3f {
            return lamp.color
        }

        override fun initModels(gl: WebGL2RenderingContext) {
            val size = 10f
            particleModel = TexturedModel(
                    ModelCreator.loadModel(gl,
                            vertices = arrayOf(
                                    -size, -size, 0f, // bottom left corner
                                    -size, size, 0f, // top left corner
                                    size, size, 0f, // top right corner
                                    size, -size, 0f // bottom right corner
                            ),
                            textureCoords = arrayOf(
                                    0f, 1f, // bottom left corner
                                    0f, 0f, // top left corner
                                    1f, 0f, // top right corner
                                    1f, 1f // bottom right corner
                            ),
                            normals = arrayOf(),
                            indices = arrayOf(0, 1, 2, 0, 2, 3)),
                    Texture(ModelCreator.loadTexture(gl, TextureImageType.LAMP_GLOW))
            )
        }

        override fun renderParticles(gameRenderer: GameRenderer, camera: Camera) {
            lightMatrix.set(worldMatrix)
            val dir = camera.position - Vector3f(lightMatrix.m30, lightMatrix.m31, lightMatrix.m32)
            dir.normalizeLocal()
            lightMatrix.m30 += dir.x * 3.0f * worldScale.x
            lightMatrix.m31 += dir.y * 3.0f * worldScale.y
            lightMatrix.m32 += dir.z * 3.0f * worldScale.z

            gameRenderer.renderParticle(particleModel, lightMatrix, Vector4f(lamp.color, 0.5f))
        }
    }

    override var color: IVector3f by StateProperty()

    private val light = LampLight(this).apply { position = Vector3f(0f, 11f, 0f) }

    companion object {
        private val renderer = SimpleModelData(ModelType.LAMP, TextureImageType.LAMP)
    }

    init {
        addChild(light)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        light.initModels(gl)
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer, worldMatrix)
    }
}