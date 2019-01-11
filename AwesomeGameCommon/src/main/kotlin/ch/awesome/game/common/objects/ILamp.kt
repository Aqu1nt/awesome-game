package ch.awesome.game.common.objects

import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.base.IBaseObject

interface ILamp<VECTOR: IVector3f>: IBaseObject<VECTOR> {
    var color: VECTOR
}