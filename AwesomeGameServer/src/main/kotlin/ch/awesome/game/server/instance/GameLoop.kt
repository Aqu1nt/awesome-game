package ch.awesome.game.server.instance

import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep

interface Updateable {
    fun beforeUpdate() { }
    fun update(tpf: Float)
    fun afterUpdate() { }
}

class GameLoop(private val updateables: MutableList<Updateable>,
               private val ticksPerSecond: Int): Runnable {

    @Volatile
    private var thread: Thread? = null

    private val runOnTick = mutableListOf<() -> Unit>()

    fun start() {
        synchronized(this) {
            if (thread == null) {
                thread = Thread(this)
                thread?.start()
            }
            else {
                throw IllegalStateException("GameLoop is already running")
            }
        }
    }

    fun stop() {
        synchronized(this) {
            if (thread != null) {
                thread?.interrupt()
            }
            else {
                throw IllegalStateException("GameLoop is not running")
            }
        }
    }

    fun run(runnable: () -> Unit) {
        synchronized(runOnTick) {
            runOnTick.add(runnable)
        }
    }

    override fun run() {
        var lastTick: Long = currentTimeMillis()
        while (thread?.isInterrupted == false) {
            val tickStart = currentTimeMillis()
            val nextTick = tickStart + (1000.toDouble() / ticksPerSecond.toDouble()).toLong()
            val tpf = 1f / 1000f * (tickStart - lastTick)
            try {
                synchronized(runOnTick) {
                    runOnTick.forEach { it() }
                    runOnTick.clear()
                }
                for (item in updateables) {
                    item.beforeUpdate()
                }
                for (item in updateables) {
                    item.update(tpf)
                }
                for (item in updateables) {
                    item.afterUpdate()
                }
                synchronized(runOnTick) {
                    runOnTick.forEach { it() }
                    runOnTick.clear()
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            val waitForNextTick = nextTick - currentTimeMillis()
            if (waitForNextTick > 0) {
                sleep(waitForNextTick)
            }
            lastTick = tickStart
        }
    }
}