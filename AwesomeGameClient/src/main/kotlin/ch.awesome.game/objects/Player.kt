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
        val vertices = arrayOf(
                -1.0, 1.0, -1.0,
                -1.0, 1.0, 1.0,
                1.0, 1.0, 1.0,
                1.0, 1.0, -1.0,

                // Left
                -1.0, 1.0, 1.0,
                -1.0, -1.0, 1.0,
                -1.0, -1.0, -1.0,
                -1.0, 1.0, -1.0,

                // Right
                1.0, 1.0, 1.0,
                1.0, -1.0, 1.0,
                1.0, -1.0, -1.0,
                1.0, 1.0, -1.0,

                // Front
                1.0, 1.0, 1.0,
                1.0, -1.0, 1.0,
                -1.0, -1.0, 1.0,
                -1.0, 1.0, 1.0,

                // Back
                1.0, 1.0, -1.0,
                1.0, -1.0, -1.0,
                -1.0, -1.0, -1.0,
                -1.0, 1.0, -1.0,

                // Bottom
                -1.0, -1.0, -1.0,
                -1.0, -1.0, 1.0,
                1.0, -1.0, 1.0,
                1.0, -1.0, -1.0
        ).map { it.toFloat() }.toTypedArray()

        val textureCoords = arrayOf(0,0, 0,1, 1,1, 1,0, 0,0, 0,1, 1,1, 1,0, 0,0, 0,1, 1,1,
                                    1,0, 0,0, 0,1, 1,1, 1,0, 0,0, 0,1, 1,1, 1,0, 0,0, 0,1, 1,1, 1,0).map { it.toFloat() }.toTypedArray()

        val indices = arrayOf(
                // Top
                0, 1, 2,
                0, 2, 3,

                // Left
                5, 4, 6,
                6, 4, 7,

                // Right
                8, 9, 10,
                8, 10, 11,

                // Front
                13, 12, 14,
                15, 14, 12,

                // Back
                16, 17, 18,
                16, 18, 19,

                // Bottom
                21, 20, 22,
                22, 20, 23
        )

        val rawModel = ModelCreator.loadModel(gl, vertices, textureCoords, indices)
        val texture = ModelCreator.loadTexture(gl, TextureImage.CRATE)
        model = TexturedModel(rawModel, texture)

        val grassVertices = arrayOf(-1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0).map { it.toFloat() }.toTypedArray()
        val grassTextureCoords = arrayOf(0,0, 0,1, 1,1, 1,0).map { it.toFloat() }.toTypedArray()
        val grassIndices = arrayOf(0, 1, 2, 0, 2, 3)

        val grassModel = ModelCreator.loadModel(gl, grassVertices, grassTextureCoords, grassIndices)
        val grassTexture = ModelCreator.loadTexture(gl,TextureImage.GRASS)
        grass = TexturedModel(grassModel, grassTexture)

        OBJModelLoader.load(gl, "boulder.obj")
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