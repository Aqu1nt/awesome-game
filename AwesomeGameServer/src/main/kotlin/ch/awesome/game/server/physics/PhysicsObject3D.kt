package ch.awesome.game.server.physics

import ch.awesome.game.common.math.IVector3f

interface PhysicsObject3D {
    val position: IVector3f
    val scale: IVector3f
}