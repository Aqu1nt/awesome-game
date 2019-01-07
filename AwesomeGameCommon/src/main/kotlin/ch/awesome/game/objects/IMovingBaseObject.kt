package ch.awesome.game.objects

import ch.awesome.game.utils.IVector3f

interface IMovingBaseObject {
    var position: IVector3f
    var velocity: IVector3f
    var unitPerSecond: Float
}