package ch.awesome.game.client.rendering.loading

import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.Model
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.common.math.Vector2f
import ch.awesome.game.common.math.Vector3f
import kotlin.browser.window
import kotlin.js.Promise

enum class ModelType(val fileName: String) {
    BOULDER("boulder.evmdl"),
    LAMP("lamp.evmdl"),
    CUBE("cube.evmdl"),
    PLAYER("running.evmdl"),
    JELLYFISH("jellyfish.evmdl"),
    BULLET("bullet.evmdl"),
    PLAYER_ARMOR("player_armor.evmdl")
}

object ModelLoader {

    private val models = mutableMapOf<ModelType, Model>()

    fun loadAllModels(gl: WebGL2RenderingContext): Promise<Array<out Unit>> {
        return Promise.all(ModelType.values().map { model ->
            ModelLoader.load(gl, model.fileName).then { data ->
                models[model] = data
            }
        }.toTypedArray())
    }

    fun getModel(model: ModelType): Model {
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
        val vertices = mutableListOf<Vector3f>()
        val textureCoords = mutableListOf<Vector2f>()
        val normals = mutableListOf<Vector3f>()
        val indices = mutableListOf<Int>()

        val lines = data.split("\n")

        for (line in lines) {
            when {
                line.startsWith("vertices") -> {
                    val linesplit = line.split("vertices ")[1].split(" ")

                    for (i in 0 until linesplit.size step 3) {
                        vertices.add(Vector3f(linesplit[i].toFloat(), linesplit[i + 1].toFloat(), linesplit[i + 2].toFloat()))
                    }
                }
                line.startsWith("texturecoords") -> {
                    val linesplit = line.split("texturecoords ")[1].split(" ")

                    for (i in 0 until linesplit.size step 2) {
                        textureCoords.add(Vector2f(linesplit[i].toFloat(), linesplit[i + 1].toFloat()))
                    }
                }
                line.startsWith("normals") -> {
                    val linesplit = line.split("normals ")[1].split(" ")

                    for (i in 0 until linesplit.size step 3) {
                        normals.add(Vector3f(linesplit[i].toFloat(), linesplit[i + 1].toFloat(), linesplit[i + 2].toFloat()))
                    }
                }
                line.startsWith("indices") -> {
                    val linesplit = line.split("indices ")[1].split(" ")

                    for (i in 0 until linesplit.size) {
                        indices.add(linesplit[i].toInt())
                    }
                }
            }
        }

        val vertexArray = FloatArray(vertices.size * 3).toTypedArray()
        val textureCoordsArray = FloatArray(vertices.size * 2).toTypedArray()
        val normalArray = FloatArray(vertices.size * 3).toTypedArray()
        val indicesArray = IntArray(indices.size).toTypedArray()

        for (i in 0 until vertices.size) {
            vertexArray[i * 3] = vertices[i].x
            vertexArray[i * 3 + 1] = vertices[i].y
            vertexArray[i * 3 + 2] = vertices[i].z
        }

        for (i in 0 until textureCoords.size) {
            textureCoordsArray[i * 2] = textureCoords[i].x
            textureCoordsArray[i * 2 + 1] = textureCoords[i].y
        }

        for (i in 0 until normals.size) {
            normalArray[i * 3] = normals[i].x
            normalArray[i * 3 + 1] = normals[i].y
            normalArray[i * 3 + 2] = normals[i].z
        }

        for (i in 0 until indices.size) {
            indicesArray[i] = indices[i]
        }

        return ModelCreator.loadModel(gl, vertexArray, textureCoordsArray, normalArray, indicesArray)
    }
}