package ch.awesome.game.client.rendering.loading

import ch.awesome.game.client.rendering.textures.TextureData
import org.khronos.webgl.get
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.ImageData
import kotlin.browser.document
import kotlin.js.Promise

enum class TextureImageType(val fileName: String) {
    CRATE("crate.png"),
    GRASS("grass.png"),
    BOULDER("boulder.png"),
    LAMP("lamp.png"),
    LAMP_LIGHTMAP("lamp_lightmap.png"),
    CUBE("cube.png"),
    PLAYER("running.png"),
    PLAYER_LIGHTMAP("player_lightmap.png"),
    PLAYER_ARMOR("player_armor.png"),
    PLAYER_ICON("playericon.png"),
    JELLYFISH("jellyfish.png"),
    LAMP_GLOW("lamp_glow.png"),
    BULLET("bullet.png"),
    OCEAN_GROUND("ocean_ground.png"),
    OCEAN_GROUND_LIGHTMAP("ocean_ground_lightmap.png"),
    HEIGHTMAP("heightmap.png"),
    SKYBOX_RIGHT("skybox/right.png"),
    SKYBOX_LEFT("skybox/left.png"),
    SKYBOX_TOP("skybox/top.png"),
    SKYBOX_BOTTOM("skybox/bottom.png"),
    SKYBOX_BACK("skybox/back.png"),
    SKYBOX_FRONT("skybox/front.png"),
    HEALTH_BAR("guis/healthbar.png"),
    FONT("guis/font.png"),
}

object TextureImageLoader {

    private val textures = mutableMapOf<TextureImageType, TextureData>()

    fun loadAllTextureImages(): Promise<Array<out Unit>> {
        return Promise.all(TextureImageType.values().map { textureImage ->
            load(textureImage.fileName).then { data ->
                textures[textureImage] = data
            }
        }.toTypedArray())
    }

    fun getTextureImage(textureImage: TextureImageType): TextureData {
        return textures[textureImage] ?: throw IllegalStateException("Did not find texture $textureImage!")
    }

    private fun load(fileName: String): Promise<TextureData> {
        return Promise { resolve, _ ->
            val img = document.createElement("img") as HTMLImageElement
            img.onload = {
                val canvas = document.createElement("canvas") as HTMLCanvasElement
                val context = canvas.getContext("2d") as CanvasRenderingContext2D
                canvas.width = img.width
                canvas.height = img.height
                context.drawImage(img, 0.0, 0.0)
                val data = context.getImageData(0.0, 0.0, img.width.toDouble(), img.height.toDouble())
                resolve(TextureData(data, data.width, data.height))
            }

            img.src = "res/textures/$fileName"
        }
    }
}