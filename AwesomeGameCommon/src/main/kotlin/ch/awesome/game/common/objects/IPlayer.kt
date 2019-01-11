package ch.awesome.game.common.objects

import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.objects.base.IMovingBaseObject

interface IPlayer<VECTOR: IVector3f>: IMovingBaseObject<VECTOR> {

}