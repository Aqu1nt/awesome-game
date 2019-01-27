package ch.awesome.game.client.lib

import org.w3c.dom.events.MouseEvent

@JsName("MouseEvent")
abstract external class LockedMouseEvent : MouseEvent {

    var movementX: Int
    var movementY: Int
}