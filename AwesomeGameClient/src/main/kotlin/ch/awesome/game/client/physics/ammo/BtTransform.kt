@file:JsQualifier("Ammo")
package ch.awesome.game.client.physics.ammo

@JsName("btTransform")
external class BtTransform() {

    fun setOrigin(origin: BtVector3f)
    fun getOrigin(): BtVector3f
}