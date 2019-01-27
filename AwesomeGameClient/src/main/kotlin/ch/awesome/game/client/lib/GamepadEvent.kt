package ch.awesome.game.client.lib

import org.w3c.dom.events.Event

abstract external class GamepadEvent : Event {

    var gamepad: Gamepad
}