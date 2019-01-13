package ch.awesome.game.server.instance

import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.server.objects.SLamp
import ch.awesome.game.server.objects.SPlayer
import ch.awesome.game.server.objects.SWorld
import ch.awesome.game.server.objects.base.SMovingGroup
import ch.awesome.game.server.physics.GamePhysics
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

val GAME = StandardGame().apply {
    thread {
        initScene()
        loop.start()
    }
}

class StandardGame: Updateable {

    val world = SWorld()
    val physics = GamePhysics()
    val loop = GameLoop(mutableListOf(this), 100)

    fun initScene(){
        val group = SMovingGroup().apply {
            addChild(SLamp().apply { position = Vector3f(-10.0f, 0.0f, 0.0f); color = Vector3f(1.0f, 0.9f, 0.4f) })
            addChild(SLamp().apply { position = Vector3f(10.0f, 0.0f, 0.0f); color = Vector3f(1.0f, 0.0f, 0.0f) })
            rotationVelocity = Vector3f(0f, 30f, 0f)
        }
        world.addChild(group)
    }

    fun join(): CompletableFuture<SPlayer> {
        val player = SPlayer()
        val future = CompletableFuture<SPlayer>()
        loop.run {
            world.addChild(player)
            future.complete(player)
        }
        return future
    }

    fun leave(player: SPlayer) {
        loop.run {
            world.removeChild(player)
        }
    }

    override fun beforeUpdate() {
        world.beforeUpdate()
    }

    override fun update(tpf: Float) {
        world.update(tpf)
        world.calculateWorldMatrix()
    }

    override fun afterUpdate() {
        world.afterUpdate()
        physics.detectCollisions()
        world.sendChangesToClients()
    }
}