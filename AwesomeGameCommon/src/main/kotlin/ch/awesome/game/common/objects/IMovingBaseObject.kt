package ch.awesome.game.common.objects

import ch.awesome.game.common.math.IVector3f

interface IMovingBaseObject<T: IVector3f>: IBaseObject<T> {
    var velocity: T
    var unitPerSecond: Float

    var rotationVelocity: T
    var rotationUnitPerSecond: Float

    var scaleVelocity: T
    var scaleUnitPerSecond: Float
}