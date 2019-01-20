package ch.awesome.game.client.physics

import ch.awesome.game.client.physics.ammo.*

class AmmoPhysics {

    private val collisionConfiguration = BtDefaultCollisionConfiguration()
    private val dispatcher = BtCollisionDispatcher(collisionConfiguration)
    private val overlappingPairCache = BtDbvtBroadphase()
    private val solver = BtSequentialImpulseConstraintSolver()
    val dynamicsWorld = BtDiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration)

    init {
        dynamicsWorld.setGravity(BtVector3f(0f, 0f, 0f))
    }

    fun detectCollisions() {
//        dynamicsWorld.performDiscreteCollisionDetection()
        dynamicsWorld.stepSimulation(1f / 60f)
    }
}