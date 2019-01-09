package ch.awesome.game.common.objects

import ch.awesome.game.common.math.IVector3f

interface IMovingBaseObject {
    var position: IVector3f
    var velocity: IVector3f
    var unitPerSecond: Float
}