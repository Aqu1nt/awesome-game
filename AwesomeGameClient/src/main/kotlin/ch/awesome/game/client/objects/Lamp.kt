package ch.awesome.game.client.objects

import ch.awesome.game.client.objects.base.LightNode
import ch.awesome.game.client.rendering.GameRenderer
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.Texture
import ch.awesome.game.client.rendering.TexturedModel
import ch.awesome.game.client.rendering.loading.ModelType
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.SimpleModelRenderer
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.StateProperty
import ch.awesome.game.client.state.interfaces.Renderer
import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.math.Vector4f
import ch.awesome.game.common.objects.ILamp

class Lamp(state: dynamic) : GameNode(state), ILamp<IVector3f>, Renderer {

    class LampLight(private val lamp: Lamp) : LightNode(attenuation = Vector3f(1f, 0.01f, 0.00001f)), Renderer {

        lateinit var particleModel: TexturedModel

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
                            normals = arrayOf(
                                    0f, 0f, -1f, // bottom left corner
                                    0f, 0f, -1f, // top left corner
                                    0f, 0f, -1f, // top right corner
                                    0f, 0f, -1f // bottom right corner
                            ),
                            indices = arrayOf(0, 1, 2, 0, 2, 3)),
                    Texture(ModelCreator.loadTexture(gl, TextureImageType.FIRE_PARTICLE))
            )
        }

        override fun render(gameRenderer: GameRenderer) {
            gameRenderer.renderParticle(particleModel, worldMatrix, Vector4f(lamp.color, 0.5f))
        }
    }

    override var color: IVector3f by StateProperty()

    private val light = LampLight(this).apply { position = Vector3f(0f, 14f, 0f) }
    private val renderer = SimpleModelRenderer(this, ModelType.LAMP, TextureImageType.LAMP)

    init {
        addChild(light)
    }

    override fun initModels(gl: WebGL2RenderingContext) {
        light.initModels(gl)
        renderer.initModels(gl)
    }

    override fun render(gameRenderer: GameRenderer) {
        renderer.render(gameRenderer)
    }
}