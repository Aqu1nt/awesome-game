@file:JsQualifier("Ammo")
package ch.awesome.game.client.physics.ammo

external interface BtMotionState {

    fun getWorldTransform(out: BtTransform)
    fun setWorldTransform(transform: BtTransform)
}

@JsName("btDefaultMotionState")
external class BtDefaultMotionState(transform: BtTransform): BtMotionState {

    override fun getWorldTransform(out: BtTransform)
    override fun setWorldTransform(transform: BtTransform)
}