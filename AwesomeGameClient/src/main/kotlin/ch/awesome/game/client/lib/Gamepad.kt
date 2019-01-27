package ch.awesome.game.client.lib

abstract external class Gamepad {

    var id: String
    var index: Int
    var connected: Boolean
    var buttons: Array<GamepadButton>
    var axes: Array<Double>
}