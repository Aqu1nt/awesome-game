package ch.awesome.game.client.rendering.loading

import ch.awesome.game.common.math.Vector3f

class OBJModelLoaderVertex(val position: Vector3f, val index: Int) {

    var textureCoordsIndex = -1
    var normalIndex = -1
    var clone: OBJModelLoaderVertex? = null
    var length = 0.0f

    init {
        length = position.length()
    }

    fun isSet(): Boolean {
        return textureCoordsIndex != -1 && normalIndex != -1
    }

    fun same(textureCoordsIndex: Int, normalIndex: Int): Boolean {
        return this.textureCoordsIndex == textureCoordsIndex && this.normalIndex == normalIndex
    }
}