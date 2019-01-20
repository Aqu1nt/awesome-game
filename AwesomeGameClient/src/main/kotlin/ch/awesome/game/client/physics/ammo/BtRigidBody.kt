@file:JsQualifier("Ammo")
package ch.awesome.game.client.physics.ammo

@JsName("btRigidBody")
external class BtRigidBody(mass: Float,
                           motionState: BtMotionState,
                           shape: BtCollisionShape): BtCollisionObject {

}