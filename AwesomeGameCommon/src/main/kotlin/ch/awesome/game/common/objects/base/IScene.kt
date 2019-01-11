package ch.awesome.game.common.objects.base

import ch.awesome.game.common.math.IVector3f

interface IScene<VECTOR: IVector3f>: IBaseObject<VECTOR> {
    var ambientLight: Float
}