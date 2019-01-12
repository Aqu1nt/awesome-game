package ch.awesome.game.server.objects.base

import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Quaternion
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.base.IBaseObject
import ch.awesome.game.server.instance.Updateable
import ch.awesome.game.server.utils.SmartProperty
import ch.awesome.game.server.utils.SmartTreeItem
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

open class BaseObject(id: String = UUID.randomUUID().toString()): SmartTreeItem(id), Updateable, IBaseObject<Vector3f> {

    override var position: Vector3f by SmartProperty(Vector3f())
    override var scale: Vector3f by SmartProperty(Vector3f(1.0f, 1.0f, 1.0f))
    override var rotation: Vector3f by SmartProperty(Vector3f())

    val worldScale = Vector3f()
    val worldRotation = Quaternion()
    val worldTranslation = Vector3f()
    val worldMatrix = Matrix4f().identity()

    fun calculateWorldMatrix() {
        val finalParent = parent

        worldScale.set(scale)
        worldTranslation.set(position)
        worldRotation.fromAngles(rotation).inverseLocal()

        if (finalParent != null && finalParent is BaseObject) {
            worldScale.multLocal(finalParent.worldScale)
            finalParent.worldRotation.mult(worldRotation, worldRotation)
            worldTranslation.multLocal(finalParent.worldScale)
            finalParent.worldRotation
                    .multLocal(worldTranslation)
                    .addLocal(finalParent.worldTranslation)
        }

        worldMatrix.identity()
        worldMatrix.translate(worldTranslation)
        worldRotation.inverse().toRotationMatrix(worldMatrix)
        worldMatrix.scale(worldScale)

        for (child in children) {
            if (child is BaseObject) {
                child.calculateWorldMatrix()
            }
        }
    }

    @JsonProperty("type")
    fun getType(): String {
        return javaClass.simpleName
    }

    override fun beforeUpdate() {
        for (child in children) {
            if (child is Updateable) {
                child.beforeUpdate()
            }
        }
    }

    override fun update(tpf: Float) {
        for (child in children) {
            if (child is Updateable) {
                child.update(tpf)
            }
        }
    }

    override fun afterUpdate() {
        for (child in children) {
            if (child is Updateable) {
                child.afterUpdate()
            }
        }
    }
}