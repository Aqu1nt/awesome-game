package ch.awesome.game.server.physics

import ch.awesome.game.server.objects.base.SBaseObject
import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.bulletphysics.dynamics.*
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f


class GamePhysics {

    val dynamicsWorld: DynamicsWorld
    private val broadPhase = DbvtBroadphase()
    private val collisionConfiguration = DefaultCollisionConfiguration()
    private val dispatcher = CollisionDispatcher(collisionConfiguration)
    private val solver = SequentialImpulseConstraintSolver()

    init {
        dynamicsWorld = DiscreteDynamicsWorld(dispatcher, broadPhase, solver, collisionConfiguration)
        dynamicsWorld.setGravity(Vector3f(0f, -10f, 0f))

        dynamicsWorld.setInternalTickCallback(object : InternalTickCallback() {
            override fun internalTick(dynamicsWorld: DynamicsWorld, timeStep: Float) {
                handleCollisions(dynamicsWorld)
            }
        }, null)

        initializeGround()
    }

    private fun initializeGround() {
        val groundShape = StaticPlaneShape(Vector3f(0f, 1f, 0f), 0.25f)
        val groundMotionState = DefaultMotionState(Transform(Matrix4f(
                Quat4f(0f, 0f, 0f, 1f),
                Vector3f(0f, 0f, 0f),
                1f
        )))
        val groundBodyConstructionInfo = RigidBodyConstructionInfo(0f, groundMotionState, groundShape, Vector3f(0f, 0f, 0f))
        groundBodyConstructionInfo.restitution = 0f
        val groundRigidBody = RigidBody(groundBodyConstructionInfo)
        dynamicsWorld.addRigidBody(groundRigidBody)
    }

    fun detectCollisions() {
        dynamicsWorld.performDiscreteCollisionDetection()
        handleCollisions(dynamicsWorld)
    }

    fun handleCollisions(dynamicsWorld: DynamicsWorld) {
        val dispatcher = dynamicsWorld.dispatcher
        val manifoldCount = dispatcher.numManifolds
        for (i in 0 until manifoldCount) {
            val manifold = dispatcher.getManifoldByIndexInternal(i)
            // The following two lines are optional.
            val object1 = manifold.body0 as CollisionObject
            val object2 = manifold.body1 as CollisionObject
            val physicsObject1 = object1.userPointer
            val physicsObject2 = object2.userPointer

            if (physicsObject1 is SBaseObject && physicsObject2 is SBaseObject) {
                var hit = false
                var normal: Vector3f? = null
                for (j in 0 until manifold.numContacts) {
                    val contactPoint = manifold.getContactPoint(j)
                    if (contactPoint.distance < 0.0f) {
                        hit = true
                        normal = contactPoint.normalWorldOnB
                        break
                    }
                }
                if (hit) {
                    println("Collision ... $physicsObject1 $physicsObject2 $normal")
                }
            }

        }
    }
}