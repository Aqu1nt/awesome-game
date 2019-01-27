package ch.awesome.game.client.lib

import org.w3c.dom.HTMLCanvasElement

@JsName("HTMLCanvasElement")
abstract external class GameCanvasElement : HTMLCanvasElement {

    fun requestPointerLock()

}