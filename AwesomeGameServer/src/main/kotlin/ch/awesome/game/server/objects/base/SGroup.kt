package ch.awesome.game.server.objects.base

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.objects.base.IGroup
import ch.awesome.game.server.utils.withSmartProperties

class SGroup: SBaseObject(), IGroup<Vector3f> {

    init { withSmartProperties() }
}