package ch.awesome.game.client.objects.base

import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.models.Model
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.loading.TextureImageLoader
import ch.awesome.game.client.rendering.textures.Texture
import ch.awesome.game.client.rendering.loading.TextureImageType
import ch.awesome.game.client.rendering.renderer.GameRenderer
import ch.awesome.game.client.state.GameNode
import ch.awesome.game.client.state.interfaces.Renderable
import ch.awesome.game.common.math.Vector3f

class CTerrain(val gl: WebGL2RenderingContext, state: dynamic): GameNode(state), Renderable {

    companion object {
        val SIZE = 800.0f
        val MAX_HEIGHT = 40.0f
        val MAX_PIXEL_COLOR = 256
    }

    override var animated = false

    var x = 0.0f
    var z = 0.0f
    val heightmap = TextureImageLoader.getTextureImage(TextureImageType.HEIGHTMAP)
    val model = generateTerrain()
    val texture = Texture(modelTexture = ModelCreator.loadTexture(gl, TextureImageType.OCEAN_GROUND),
            lightMap = ModelCreator.loadTexture(gl, TextureImageType.OCEAN_GROUND_LIGHTMAP))

    fun generateTerrain(): Model {
        val vertexCountPerSide = heightmap.width
        val vertexCount = vertexCountPerSide * vertexCountPerSide
        val vertices = FloatArray(vertexCount * 3)
        val textureCoords = FloatArray(vertexCount * 2)
        val normals = FloatArray(vertexCount * 3)
        val indices = IntArray(6 * (vertexCountPerSide - 1) * (vertexCountPerSide - 1))

        var vertexPointer = 0
        var indexPointer = 0

        for (z in 0 until vertexCountPerSide) {
            for (x in 0 until vertexCountPerSide) {
                vertices[vertexPointer * 3] = x.toFloat() / (vertexCountPerSide.toFloat() - 1) * SIZE
                vertices[vertexPointer * 3 + 1] = getHeight(x, z)
                vertices[vertexPointer * 3 + 2] = z.toFloat() / (vertexCountPerSide.toFloat() - 1) * SIZE

                textureCoords[vertexPointer * 2] = x.toFloat() / (vertexCountPerSide.toFloat() - 1)
                textureCoords[vertexPointer * 2 + 1] = z.toFloat() / (vertexCountPerSide.toFloat() - 1)

                val normal = getNormal(x, z)
                normals[vertexPointer * 3] = normal.x
                normals[vertexPointer * 3 + 1] = normal.y
                normals[vertexPointer * 3 + 2] = normal.z

                vertexPointer ++
            }
        }

        for(iz in 0 until vertexCountPerSide - 1) {
            for(ix in 0 until vertexCountPerSide - 1) {
                val topLeft = (iz * vertexCountPerSide) + ix
                val topRight = topLeft + 1
                val bottomLeft = ((iz + 1) * vertexCountPerSide) + ix
                val bottomRight = bottomLeft + 1

                indices[indexPointer ++] = topLeft
                indices[indexPointer ++] = bottomLeft
                indices[indexPointer ++] = topRight
                indices[indexPointer ++] = topRight
                indices[indexPointer ++] = bottomLeft
                indices[indexPointer ++] = bottomRight
            }
        }

        return ModelCreator.loadModel(gl, vertices.toTypedArray(), textureCoords.toTypedArray(), normals.toTypedArray(), indices.toTypedArray())
    }

    fun getHeight(x: Int, z: Int): Float {
        if (x < 0 || x >= heightmap.width || z < 0 || z >= heightmap.height) {
            return 0.0f
        }

        var height = heightmap.getColor(x, z).r.toFloat()
        height += MAX_PIXEL_COLOR / 2.0f
        height /= MAX_PIXEL_COLOR / 2.0f
        height *= MAX_HEIGHT

        return height
    }

    fun getNormal(x: Int, z: Int): Vector3f {
        val heightLeft = getHeight(x - 1, z)
        val heightRight = getHeight(x + 1, z)
        val heightBack = getHeight(x, z - 1)
        val heightFront = getHeight(x, z + 1)

        return Vector3f(heightLeft - heightRight, 2.0f, heightBack - heightFront).normalize()
    }

//    override fun render(gameRenderer: GameRenderer) {
//        gameRenderer.renderTerrain(this, modelMatrix)
//    }
}