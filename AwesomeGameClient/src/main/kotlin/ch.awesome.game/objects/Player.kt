package ch.awesome.game.objects

import ch.awesome.game.engine.rendering.*
import ch.awesome.game.lib.utils.Vector3f
import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.state.GameNode
import ch.awesome.game.state.StateProperty
import ch.awesome.game.state.interfaces.Renderable

class Player(state: dynamic): GameNode(state), Renderable {

    private var age by StateProperty<Int>()
    private var name by StateProperty<String>()
    private var position by StateProperty<Vector3f>()

    lateinit var model: TexturedModel
    lateinit var grass: TexturedModel

    override fun initModels(gl: WebGL2RenderingContext) {
        val texture = ModelCreator.loadTexture(gl, TextureImageType.BOULDER)
        model = TexturedModel(OBJModelLoader.getModel(ModelType.BOULDER), texture)

//        val grassVertices = arrayOf(-1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0).map { it.toFloat() }.toTypedArray()
//        val grassTextureCoords = arrayOf(0,0, 0,1, 1,1, 1,0).map { it.toFloat() }.toTypedArray()
//        val grassIndices = arrayOf(0, 1, 2, 0, 2, 3)
//
//        val grassModel = ModelCreator.loadModel(gl, grassVertices, grassTextureCoords, grassIndices)
//        val grassTexture = ModelCreator.loadTexture(gl,TextureImageType.GRASS)
//        grass = TexturedModel(grassModel, grassTexture)
    }

    override fun render(renderer: GameRenderer) {
        renderer.render(model, position.x, position.y, position.z)

//        for (y in -10..10) {
//            for (x in -10..10) {
//                renderer.render(grass, x * 2.0f, -2.0f, y * 2.0f)
//            }
//        }
    }
}