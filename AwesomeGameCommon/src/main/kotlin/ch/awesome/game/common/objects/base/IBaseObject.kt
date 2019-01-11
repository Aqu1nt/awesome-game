package ch.awesome.game.common.objects.base

import ch.awesome.game.common.math.IVector3f

interface IBaseObject<VECTOR: IVector3f> {
    val id: String

    var position: VECTOR
    var scale: VECTOR
    var rotation: VECTOR
}