package ch.awesome.game.client.rendering

import ch.awesome.game.client.webgl2.WebGL2RenderingContext
import ch.awesome.game.common.math.Vector2f
import ch.awesome.game.common.math.Vector3f
import kotlin.browser.window
import kotlin.js.Promise

enum class ModelType(val fileName: String) {
    BOULDER("boulder.obj"),
    BUNNY("bunny.obj"),
    LAMP("lamp.obj")
}

object OBJModelLoader {

    private val models = mutableMapOf<ModelType, Model>()

    fun loadAllModels(gl: WebGL2RenderingContext): Promise<Array<out Unit>> {
        return Promise.all(ModelType.values().map { model ->
            load(gl, model.fileName).then { data ->
                models[model] = data
            }
        }.toTypedArray())
    }

    fun getModel(model: ModelType): Model {
        return models[model] ?: throw IllegalStateException("Did not find model $model!")
    }

    fun load(gl: WebGL2RenderingContext, fileName: String): Promise<Model> {
        return Promise { resolve, reject ->
             window.fetch("res/$fileName").then { response ->
                response.text().then { text ->
                    val model = convertToModel(gl, text)
                    resolve(model)
                }
            }
        }
    }

    fun convertToModel(gl: WebGL2RenderingContext, data: String): Model {

        console.log("loading model")

        val vertices = arrayListOf<Vector3f>()
        val textureCoords = arrayListOf<Vector2f>()
        val normals = arrayListOf<Vector3f>()
        val indices = arrayListOf<Int>()

        val lines = data.split("\n")

        console.log("splitted lines")

        for (line in lines) {
            val linesplit = line.split(" ")

            if (line.startsWith("v ")) {
                vertices.add(Vector3f(linesplit[1].toFloat(), linesplit[2].toFloat(), linesplit[3].toFloat()))
            } else if (line.startsWith("vt ")) {
                textureCoords.add(Vector2f(linesplit[1].toFloat(), linesplit[2].toFloat()))
            } else if (line.startsWith("vn")) {
                normals.add(Vector3f(linesplit[1].toFloat(), linesplit[2].toFloat(), linesplit[3].toFloat()))
            }
        }

        console.log("finished loading vertices, texture coords and normals")

        val vertexArray = FloatArray(vertices.size * 3).toTypedArray()
        val textureCoordsArray = FloatArray(vertices.size * 2).toTypedArray()
        val normalsArray = FloatArray(vertices.size * 3).toTypedArray()
        val indicesArray = IntArray(indices.size).toTypedArray()

        for(line in lines) {
            if(line.startsWith("f ")) {
                val linesplit = line.split(" ")

                val vertex1 = linesplit[1].split("/")
                val vertex2 = linesplit[2].split("/")
                val vertex3 = linesplit[3].split("/")

                createVertex(vertex1, textureCoords, normals, indices, textureCoordsArray, normalsArray)
                createVertex(vertex2, textureCoords, normals, indices, textureCoordsArray, normalsArray)
                createVertex(vertex3, textureCoords, normals, indices, textureCoordsArray, normalsArray)
            }
        }

        console.log("finished loading faces")

        var vertexPointer = 0;
        for(vertex in vertices) {
            vertexArray[vertexPointer ++] = vertex.x
            vertexArray[vertexPointer ++] = vertex.y
            vertexArray[vertexPointer ++] = vertex.z
        }

        var indexPointer = 0
        for(index in indices) {
            indicesArray[indexPointer ++] = index
        }

        console.log("loaded and stored all data")

        return ModelCreator.loadModel(gl, vertexArray, textureCoordsArray, normalsArray, indicesArray)
    }

    private fun createVertex(vertexData: List<String>, textureCoords: ArrayList<Vector2f>, normals: ArrayList<Vector3f>,
                             indices: ArrayList<Int>, textureCoordsArray: Array<Float>, normalsArray: Array<Float>) {
        val index = vertexData[0].toInt() - 1
        indices.add(index)
        val textureCoord = textureCoords[vertexData[1].toInt() - 1]
        textureCoordsArray[index * 2] = textureCoord.x
        textureCoordsArray[index * 2 + 1] = textureCoord.y
        val normal = normals[vertexData[2].toInt() - 1]
        normalsArray[index * 3] = normal.x
        normalsArray[index * 3 + 1] = normal.y
        normalsArray[index * 3 + 2] = normal.z
    }
}