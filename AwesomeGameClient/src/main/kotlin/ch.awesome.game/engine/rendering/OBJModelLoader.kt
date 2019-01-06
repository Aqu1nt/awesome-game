package ch.awesome.game.engine.rendering

import ch.awesome.game.lib.webgl2.WebGL2RenderingContext
import ch.awesome.game.utils.Vector2f
import ch.awesome.game.utils.Vector3f
import org.w3c.fetch.Request
import org.w3c.files.File
import kotlin.browser.window
import kotlin.js.Promise

object OBJModelLoader {

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

        val vertices = arrayListOf<Vector3f>()
        val textureCoords = arrayListOf<Vector2f>()
        val normals = arrayListOf<Vector3f>()
        val indices = arrayListOf<Int>()

        val lines = data.split("\n")

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
            }
        }

        return ModelCreator.loadModel(gl, vertexArray, textureCoordsArray, indicesArray)
    }

    fun createVertex(vertexData: Array<String>, textureCoords: ArrayList<Vector2f>, normals: ArrayList<Vector3f>,
                     indices: ArrayList<Int>, textureCoordsArray: Array<Float>, normalsArray: Array<Float>) {
        val index = vertexData[0].toInt() - 1
        indices.add(index)
        val textureCoord = textureCoords.get(vertexData[1].toInt() - 1)
        textureCoordsArray[index * 2] = textureCoord.x
        textureCoordsArray[index * 2 + 1] = textureCoord.y
        val normal = normals.get(vertexData[2].toInt() - 1)
        normalsArray[index * 3] = normal.x
        normalsArray[index * 3 + 1] = normal.y
        normalsArray[index * 3 + 2] = normal.z
    }
}