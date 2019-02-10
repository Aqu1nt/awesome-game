package ch.awesome.game.client.rendering.loading

import ch.awesome.game.client.lib.WebGL2RenderingContext
import ch.awesome.game.client.rendering.models.Model
import ch.awesome.game.client.rendering.ModelCreator
import ch.awesome.game.client.rendering.animation.*
import ch.awesome.game.client.rendering.models.ModelData
import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Quaternion
import ch.awesome.game.common.math.Vector2f
import ch.awesome.game.common.math.Vector3f
import kotlin.browser.window
import kotlin.js.Promise

enum class ModelType(val fileName: String) {
    BOULDER("boulder.evmdl"),
    LAMP("lamp.evmdl"),
    CUBE("cube.evmdl"),
    PLAYER("player.evmdl"),
    PLAYER_LV2("jellyfish.evmdl"),
    PLAYER_LV3("player_lv3.evmdl"),
    BULLET("bullet.evmdl"),
    PLAYER_ARMOR("player_armor.evmdl")
}

object ModelLoader {

    private val models = mutableMapOf<ModelType, ModelData>()

    fun loadAllModels(gl: WebGL2RenderingContext): Promise<Array<out Unit>> {
        return Promise.all(ModelType.values().map { model ->
            ModelLoader.load(gl, model.fileName).then { data ->
                models[model] = data
            }
        }.toTypedArray())
    }

    fun getModel(model: ModelType): ModelData {
        return models[model] ?: throw IllegalStateException("Did not find model $model!")
    }

    fun load(gl: WebGL2RenderingContext, fileName: String): Promise<ModelData> {
        return Promise { resolve, _ ->
            window.fetch("res/models/$fileName").then { response ->
                response.text().then { text ->
                    val model = convertToModel(gl, text)
                    resolve(model)
                }
            }
        }
    }

    private fun convertToModel(gl: WebGL2RenderingContext, data: String): ModelData {
        return if (data.contains("skeleton")) {
            convertAnimatedModel(gl, data)
        } else {
            convertStaticModel(gl, data)
        }
    }

    private fun convertStaticModel(gl: WebGL2RenderingContext, data: String): ModelData {
        val vertices = mutableListOf<Vector3f>()
        val textureCoords = mutableListOf<Vector2f>()
        val normals = mutableListOf<Vector3f>()
        val indices = mutableListOf<Int>()

        val lines = data.split("\n")

        for (line in lines) {
            when {
                line.startsWith("vertices") -> {
                    val rawData = line.split("vertices ")[1].split(" ")

                    for (i in 0 until rawData.size step 3) {
                        vertices.add(Vector3f(rawData[i].toFloat(), rawData[i + 1].toFloat(), rawData[i + 2].toFloat()))
                    }
                }
                line.startsWith("texturecoords") -> {
                    val rawData = line.split("texturecoords ")[1].split(" ")

                    for (i in 0 until rawData.size step 2) {
                        textureCoords.add(Vector2f(rawData[i].toFloat(), rawData[i + 1].toFloat()))
                    }
                }
                line.startsWith("normals") -> {
                    val rawData = line.split("normals ")[1].split(" ")

                    for (i in 0 until rawData.size step 3) {
                        normals.add(Vector3f(rawData[i].toFloat(), rawData[i + 1].toFloat(), rawData[i + 2].toFloat()))
                    }
                }
                line.startsWith("indices") -> {
                    val rawData = line.split("indices ")[1].split(" ")

                    for (i in 0 until rawData.size) {
                        indices.add(rawData[i].toInt())
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

        return ModelData(vertexArray, textureCoordsArray, normalArray, indicesArray)
    }

    private fun convertAnimatedModel(gl: WebGL2RenderingContext, data: String): ModelData {
        val vertices = mutableListOf<Vector3f>()
        val textureCoords = mutableListOf<Vector2f>()
        val normals = mutableListOf<Vector3f>()
        val indices = mutableListOf<Int>()
        val joints = mutableListOf<Joint>()
        val jointChildren = mutableListOf<String>()
        var rootJointID = 0
        val jointIDs = mutableListOf<Int>()
        val weights = mutableListOf<Float>()
        val animations = mutableListOf<Animation>()

        val lines = data.split("\n")

        for (line in lines) {
            when {
                line.startsWith("vertices") -> {
                    val rawData = line.split("vertices ")[1].split(" ")

                    for (i in 0 until rawData.size step 3) {
                        vertices.add(Vector3f(rawData[i].toFloat(), rawData[i + 1].toFloat(), rawData[i + 2].toFloat()))
                    }
                }
                line.startsWith("texturecoords") -> {
                    val rawData = line.split("texturecoords ")[1].split(" ")

                    for (i in 0 until rawData.size step 2) {
                        textureCoords.add(Vector2f(rawData[i].toFloat(), rawData[i + 1].toFloat()))
                    }
                }
                line.startsWith("normals") -> {
                    val rawData = line.split("normals ")[1].split(" ")

                    for (i in 0 until rawData.size step 3) {
                        normals.add(Vector3f(rawData[i].toFloat(), rawData[i + 1].toFloat(), rawData[i + 2].toFloat()))
                    }
                }
                line.startsWith("indices") -> {
                    val rawData = line.split("indices ")[1].split(" ")

                    for (i in 0 until rawData.size) {
                        indices.add(rawData[i].toInt())
                    }
                }
                line.startsWith("skeleton") -> {
                    val rawData = line.split("skeleton ")[1].split(" ")

                    for (i in 0 until rawData.size step 19) {
                        val id = rawData[i].toInt()
                        val name = rawData[i + 1]
                        val floats = FloatArray(16).toTypedArray()

                        for (j in 0..15) {
                            floats[j] = rawData[i + j + 2].toFloat()
                        }

                        val mat = Matrix4f().fromArray(floats)

                        joints.add(Joint(id, name, mat))

                        val children = rawData[i + 18]
                        jointChildren.add(children)
                    }
                }
                line.startsWith("rootJoint") -> {
                    rootJointID = line.split(" ")[1].toInt()
                }
                line.startsWith("jointIDs") -> {
                    val rawData = line.split("jointIDs ")[1].split(" ")

                    for (i in 0 until rawData.size) {
                        jointIDs.add(rawData[i].toInt())
                    }
                }
                line.startsWith("weights") -> {
                    val rawData = line.split("weights ")[1].split(" ")

                    for (i in 0 until rawData.size) {
                        weights.add(rawData[i].toFloat())
                    }
                }
                line.startsWith("animation") -> {
                    val name = line.split(" ")[1]
                    val keyFrames = mutableListOf<AnimationKeyFrame>()

                    val rawData = line.split("animation $name ")[1].split(" ")

                    for (i in 0 until rawData.size step 1 + joints.size * 17) {
                        val time = rawData[i].toFloat()
                        val pose = hashMapOf<String, JointTransform>()

                        for (j in 1 until joints.size * 17 step 17) {
                            val name = rawData[i + j]
                            val floats = FloatArray(16).toTypedArray()

                            for (f in 1..16) {
                                floats[f - 1] = rawData[i + j + f].toFloat()
                            }

                            val mat = Matrix4f().fromArray(floats)
                            val transform = JointTransform(Vector3f(mat.m30, mat.m31, mat.m32), Quaternion().fromMatrix(mat))

                            pose[name] = transform

                            for (a in joints) {
                                if (a.name == name) {
                                    a.transform = transform
                                }
                            }
                        }

                        keyFrames.add(AnimationKeyFrame(time, pose))
                    }

                    animations.add(Animation(name, keyFrames))
                }
            }
        }

        val vertexArray = FloatArray(vertices.size * 3).toTypedArray()
        val textureCoordsArray = FloatArray(vertices.size * 2).toTypedArray()
        val normalArray = FloatArray(vertices.size * 3).toTypedArray()
        val indicesArray = IntArray(indices.size).toTypedArray()
        var rootJoint: Joint? = null
        val jointIDsArray = IntArray(jointIDs.size).toTypedArray()
        val weightsArray = FloatArray(weights.size).toTypedArray()

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

        for (i in 0 until joints.size) {
            val joint = joints[i]
            val children = jointChildren[i].split("/")

            for (childID in children) {
                val id = childID.toInt()

                for (j in 0 until joints.size) {
                    val child = joints[j]

                    if (child.id == id) {
                        joint.addChild(child)
                        child.parent = joint
                    }
                }
            }

            if (joint.id == rootJointID) {
                rootJoint = joint
            }
        }

        for (i in 0 until jointIDs.size) {
            jointIDsArray[i] = jointIDs[i]
        }

        for (i in 0 until weights.size) {
            weightsArray[i] = weights[i]
        }

        val skeleton = Skeleton(joints, rootJoint!!)

        return ModelData(vertexArray, textureCoordsArray, normalArray, indicesArray, jointIDsArray, weightsArray, skeleton, animations)
    }
}