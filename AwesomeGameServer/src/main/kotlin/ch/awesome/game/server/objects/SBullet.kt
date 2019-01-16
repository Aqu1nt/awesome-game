package ch.awesome.game.server.objects

import ch.awesome.game.server.instance.GAME
import ch.awesome.game.server.objects.base.SMovingBaseObject
import ch.awesome.game.server.utils.withSmartProperties

class SBullet(val player: SPlayer): SMovingBaseObject() {

    init {
        withSmartProperties()
        scale = player.worldScale
        rotation = player.rotation
    }

    var lifeTime = 3.0f

    override fun update(tpf: Float) {
        lifeTime -= tpf
        if (lifeTime <= 0) GAME.loop.run { parent?.removeChild(this) }

        super.update(tpf)
    }

    override fun afterUpdate() {
        for(n in GAME.world.children()) {
            if(n is SPlayer && n != player && worldTranslation.distance(n.worldTranslation) <= 4.0f * n.worldScale.x) {
                GAME.loop.run {
                    n.health -= 1.0f
                    parent?.removeChild(this)
                }
            }
        }

        super.afterUpdate()
    }
}