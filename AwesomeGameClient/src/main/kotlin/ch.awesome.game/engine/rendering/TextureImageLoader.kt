package ch.awesome.game.engine.rendering

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.ImageData
import kotlin.browser.document
import kotlin.js.Promise

enum class TextureImage(val fileName: String) {
    CRATE("crate.png"),
    GRASS("grass.png")
}

object TexuteImageLoader {

    private val textures = mutableMapOf<TextureImage, ImageData>()

    fun loadAllTextureImages(): Promise<Array<out Unit>> {
        return Promise.all(TextureImage.values().map { textureImage ->
            load(textureImage.fileName).then { data ->
                textures[textureImage] = data
            }
        }.toTypedArray())
    }

    fun getTextureImage(textureImage: TextureImage): ImageData {
        return textures[textureImage] ?: throw IllegalStateException("Did not find texture $textureImage!")
    }

    private fun load(fileName: String): Promise<ImageData> {
        return Promise { resolve, reject ->
            val img = document.createElement("img") as HTMLImageElement
            img.onload = {
                val canvas = document.createElement("canvas") as HTMLCanvasElement
                val context = canvas.getContext("2d") as CanvasRenderingContext2D
                canvas.width = img.width
                canvas.height = img.height
                context.drawImage(img, 0.0, 0.0)
                val data = context.getImageData(0.0, 0.0, img.width.toDouble(), img.height.toDouble())
                resolve(data)
            }

            img.src = "res/$fileName"
        }
    }
}