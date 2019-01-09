package ch.awesome.game.common.objects

import ch.awesome.game.common.utils.IVector3f

interface IBaseObject<VECTOR: IVector3f> {
    val id: String

    var position: VECTOR
    var scale: VECTOR
    var rotation: VECTOR
}