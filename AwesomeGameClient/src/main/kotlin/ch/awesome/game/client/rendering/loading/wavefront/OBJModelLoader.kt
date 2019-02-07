package ch.awesome.game.client.rendering.loading.wavefront

import ch.awesome.game.client.rendering.models.Model
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.loading.OBJModelLoaderVertex
import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.common.math.Vector2f
import ch.awesome.game.common.math.Vector3f
import kotlin.browser.window
import kotlin.js.Promise

enum class OBJModelType(val fileName: String) {
//    BOULDER("boulder.obj"),
//    LAMP("lamp.obj"),
//    CUBE("cube.obj"),
//    PLAYER("wavefront/player.obj"),
//    JELLYFISH("jellyfish.obj"),
//    BULLET("bullet.obj"),
//    PLAYER_ARMOR("player_armor.obj")
}

object OBJModelLoader {

    private val models = mutableMapOf<OBJModelType, Model>()

    fun loadAllModels(gl: WebGL2RenderingContext): Promise<Array<out Unit>> {
        return Promise.all(OBJModelType.values().map { model ->
            load(gl, model.fileName).then { data ->
                models[model] = data
            }
        }.toTypedArray())
    }

    fun getModel(model: OBJModelType): Model {
        return models[model] ?: throw IllegalStateException("Did not find model $model!")
    }

    fun load(gl: WebGL2RenderingContext, fileName: String): Promise<Model> {
        return Promise { resolve, _ ->
             window.fetch("res/models/$fileName").then { response ->
                response.text().then { text ->
                    val model = convertToModel(gl, text)
                    resolve(model)
                }
            }
        }
    }

    private fun convertToModel(gl: WebGL2RenderingContext, data: String): Model {
        val vertices = mutableListOf<OBJModelLoaderVertex>()
        val textureCoords = mutableListOf<Vector2f>()
        val normals = mutableListOf<Vector3f>()
        val indices = mutableListOf<Int>()

        val lines = data.split("\n")

        for (line in lines) {
            val linesplit = line.split(" ")

            if (line.startsWith("v ")) {
                val vec = Vector3f(linesplit[1].toFloat(), linesplit[2].toFloat(), linesplit[3].toFloat())
                vertices.add(OBJModelLoaderVertex(vec, vertices.size))
            } else if (line.startsWith("vt ")) {
                textureCoords.add(Vector2f(linesplit[1].toFloat(), linesplit[2].toFloat()))
            } else if (line.startsWith("vn")) {
                normals.add(Vector3f(linesplit[1].toFloat(), linesplit[2].toFloat(), linesplit[3].toFloat()))
            }
        }

        for(line in lines) {
            if(line.startsWith("f ")) {
                val linesplit = line.split(" ")

                val vertex1 = linesplit[1].split("/")
                val vertex2 = linesplit[2].split("/")
                val vertex3 = linesplit[3].split("/")

                createVertex(vertex1, vertices, indices)
                createVertex(vertex2, vertices, indices)
                createVertex(vertex3, vertices, indices)
            }
        }

        removeUnused(vertices)

        val vertexArray = FloatArray(vertices.size * 3).toTypedArray()
        val textureCoordsArray = FloatArray(vertices.size * 2).toTypedArray()
        val normalArray = FloatArray(vertices.size * 3).toTypedArray()

        convertDataToArrays(vertices, textureCoords, normals, vertexArray, textureCoordsArray, normalArray)

        val indicesArray = IntArray(indices.size).toTypedArray()
        var indexPointer = 0
        for(index in indices) {
            indicesArray[indexPointer ++] = index
        }

        return ModelCreator.loadModel(gl, vertexArray, textureCoordsArray, normalArray, indicesArray)
    }

    private fun createVertex(vertexData: List<String>, vertices: MutableList<OBJModelLoaderVertex>, indices: MutableList<Int>) {
        val index = vertexData[0].toInt() - 1
        val textureCoordsIndex = vertexData[1].toInt() - 1
        val normalIndex = vertexData[2].toInt() - 1

        val vertex = vertices[index]

        if(!vertex.isSet()) {
            vertex.textureCoordsIndex = textureCoordsIndex
            vertex.normalIndex = normalIndex
            indices.add(index)
        } else {
            alreadCreated(vertex, textureCoordsIndex, normalIndex, vertices, indices)
        }
    }

    private fun alreadCreated(previous: OBJModelLoaderVertex, newTextureCoordsIndex: Int, newNormalIndex: Int,
                              vertices: MutableList<OBJModelLoaderVertex>, indices: MutableList<Int>) {
        if (previous.same(newTextureCoordsIndex, newNormalIndex)) {
            indices.add(previous.index)
        } else {
            val clone = previous.clone
            if(clone != null) {
                alreadCreated(clone, newTextureCoordsIndex, newNormalIndex, vertices, indices)
            } else {
                val newClone = OBJModelLoaderVertex(previous.position, vertices.size)
                newClone.textureCoordsIndex = newTextureCoordsIndex
                newClone.normalIndex = newNormalIndex
                previous.clone = newClone
                vertices.add(newClone)
                indices.add(newClone.index)
            }
        }
    }

    private fun removeUnused(vertices: MutableList<OBJModelLoaderVertex>) {
        for(v in vertices) {
            if(!v.isSet()) {
                v.textureCoordsIndex = 0
                v.normalIndex = 0
            }
        }
    }

    private fun convertDataToArrays(vertices: MutableList<OBJModelLoaderVertex>, textureCoords: MutableList<Vector2f>,
                                    normals: MutableList<Vector3f>, verticesArray: Array<Float>,
                                    textureCoordsArray: Array<Float>, normalsArray: Array<Float>) {
        var furthest = 0.0f
        for(i in 0 until vertices.size) {
            val vertex = vertices[i]
            if(vertex.length > furthest) {
                furthest = vertex.length
            }

            val position = vertex.position
            val actualTextureCoords = textureCoords[vertex.textureCoordsIndex]
            val normal = normals[vertex.normalIndex]

            verticesArray[i * 3] = position.x
            verticesArray[i * 3 + 1] = position.y
            verticesArray[i * 3 + 2] = position.z
            textureCoordsArray[i * 2] = actualTextureCoords.x
            textureCoordsArray[i * 2 + 1] = 1 - actualTextureCoords.y
            normalsArray[i * 3] = normal.x
            normalsArray[i * 3 + 1] = normal.y
            normalsArray[i * 3 + 2] = normal.z
        }
    }
}