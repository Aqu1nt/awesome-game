package ch.awesome.game.common.objects.base

import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f

interface IScene<VECTOR: IVector3f>: IBaseObject<VECTOR> {
    var ambientLight: Float
    var skyColor: Vector3f
}