@file:JsQualifier("Ammo")
package ch.awesome.game.client.physics.ammo

@JsName("btDiscreteDynamicsWorld")
external class BtDiscreteDynamicsWorld(dispatcherBt: BtCollisionDispatcher,
                                       overlappingPairCache: BtDbvtBroadphase,
                                       solver: BtSequentialImpulseConstraintSolver,
                                       configuration: BtDefaultCollisionConfiguration) {

    fun stepSimulation(tpf: Float)
    fun performDiscreteCollisionDetection()
    fun addRigidBody(rigidBody: BtRigidBody)
    fun removeRigidBody(rigidBody: BtRigidBody)
    fun setGravity(gravity: BtVector3f)
}