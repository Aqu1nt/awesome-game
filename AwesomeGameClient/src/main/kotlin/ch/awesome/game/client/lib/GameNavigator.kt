package ch.awesome.game.client.lib

import org.w3c.dom.Navigator

@JsName("Navigator")
abstract external class GameNavigator : Navigator {

    fun getGamepads(): Array<Gamepad>
}