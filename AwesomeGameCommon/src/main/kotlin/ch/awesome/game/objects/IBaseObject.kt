package ch.awesome.game.objects

import ch.awesome.game.utils.IVector3f

interface IBaseObject<VECTOR: IVector3f> {
    val id: String

    var position: VECTOR
    var scale: VECTOR
    var rotation: VECTOR
}