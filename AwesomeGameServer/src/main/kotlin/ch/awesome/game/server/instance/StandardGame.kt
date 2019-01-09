package ch.awesome.game.server.instance

import ch.awesome.game.server.objects.Lamp
import ch.awesome.game.server.objects.Player
import ch.awesome.game.server.objects.World
import ch.awesome.game.common.utils.Vector3f
import java.util.concurrent.CompletableFuture

val GAME = StandardGame().apply {
    loop.start()
}

class StandardGame: Updateable {

    val world = World()
    val loop = GameLoop(mutableListOf(this), 100)

    init {
        world.addChild(Lamp().apply { position = Vector3f(-10.0f, 0.0f, 10.0f); color = Vector3f(1.0f, 0.9f, 0.4f) })
        world.addChild(Lamp().apply { position = Vector3f(10.0f, 0.0f, 10.0f); color = Vector3f(1.0f, 0.0f, 0.0f) })
    }

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