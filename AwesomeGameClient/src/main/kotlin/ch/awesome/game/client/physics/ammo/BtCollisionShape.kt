@file:JsQualifier("Ammo")
package ch.awesome.game.client.physics.ammo

external interface BtCollisionShape {

}

@JsName("btBoxShape")
external class BtBoxShape(boxHalfExtents: BtVector3f): BtCollisionShape {

}