package ch.awesome.game.instance

import ch.awesome.game.objects.Player
import ch.awesome.game.objects.World
import java.util.concurrent.CompletableFuture

val GAME = StandardGame().apply {
    loop.start()
}

class StandardGame: Updateable {

    val world = World()
    val loop = GameLoop(mutableListOf(this), 60)

    fun join(): CompletableFuture<Player> {
        val player = Player()
        val future = CompletableFuture<Player>()
        loop.run {
            world.addChild(player)
            future.complete(player)
        }
        return future
    }

    fun leave(player: Player) {
        loop.run {
            world.removeChild(player)
        }
    }

    override fun beforeUpdate() {
        world.beforeUpdate()
    }

    override fun update(tpf: Float) {
        world.update(tpf)
    }

    override fun afterUpdate() {
        world.afterUpdate()
    }
}