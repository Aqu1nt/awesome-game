package ch.awesome.game.client.rendering.textures

import org.khronos.webgl.get
import org.w3c.dom.ImageData

class TextureData (val data: ImageData, val width: Int, val height: Int) {

    fun getColor(x: Int, y: Int): Color {
        val pixel = (x + width * y) * 4
        val r = data.data[pixel].toInt()
        val g = data.data[pixel + 1].toInt()
        val b = data.data[pixel + 2].toInt()
        val a = data.data[pixel + 3].toInt()

        return Color(r, g, b, a)
    }
}