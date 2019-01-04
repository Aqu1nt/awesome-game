package ch.awesome.game

import ch.awesome.game.networking.NetworkClient
import ch.awesome.game.utils.WebGL2RenderingContext
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

class GameClient {

    fun startGame() {
        NetworkClient.connect()

        val canvas = document.getElementById("game-canvas") as HTMLCanvasElement?
        if (canvas != null) {
            val gl = canvas.getContext("webgl2") as WebGL2RenderingContext

            canvas.width = window.innerWidth
            canvas.height = window.innerHeight

            gl.viewport(0, 0, canvas.width, canvas.height)
            gl.clearColor(1f, 0f, 1f, 1f)
            gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)
        }
        else {
            throw IllegalStateException("Game canvas not found!")
        }
    }
}
