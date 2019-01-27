package ch.awesome.game.client.state.input

import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.client.state.GameState
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.math.toRadians
import ch.awesome.game.common.network.events.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@ImplicitReflectionSerializer
class PlayerControl(val state: GameState,
                    val networkClient: NetworkClient,
                    val inputHandler: InputHandler) {

    var oldAngle = 0.0f

    init {
        inputHandler.addInputListener(object: InputListener {
            override fun mousePressed(button: Int) {
            }

            override fun mouseReleased(button: Int) {
            }

            override fun mouseMoved(x: Int, y: Int) {
                if(inputHandler.isMouseButtonPressed(InputHandler.MOUSE_BUTTON_LEFT)) {
                    state.camera.pitch += y / 5
                    state.camera.angleAround -= x / 5
                } else {
                    networkClient.sendEvent(PlayerDirectionChangeNetworkEvent().apply {
                        val angle = oldAngle + (x / 5.0f)
                        oldAngle = angle

                        val x = sin(toRadians(angle - 180.0f))
                        val z = -cos(toRadians(angle - 180.0f))
                        val vec = Vector3f(x, 0.0f, z)

                        payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
                    }, PlayerDirectionChangeNetworkEvent::class.serializer())
                }
            }

            override fun mouseWheel(delta: Double) {
                state.camera.distanceFromPlayer += delta.toFloat() / 200.0f
            }

            override fun keyPressed(key: Int) {

            }

            override fun keyReleased(key: Int) {

            }

            override fun gamepadButtonPressed(button: Int) {
                if(button == 0) {
                    networkClient.sendEvent(PlayerShootNetworkEvent(), PlayerShootNetworkEvent::class.serializer())
                }
            }

            override fun gamepadButtonReleased(button: Int) {
            }

            override fun gamepadAxisChanged(axis: Int, value: Double) {
            }
        })

        val eventListener: (Event) -> Unit = { event ->
            val keyEvent = event as KeyboardEvent
            val networkEvent = when (keyEvent.keyCode) {
                InputHandler.KEY_W -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(unitPerSec = 10.0f)
                    }
                }
                InputHandler.KEY_S -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(unitPerSec = -10.0f)
                    }
                }
                InputHandler.KEY_D -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        val angle = state.player!!.rotation.y + 10.0f

                        val x = sin(toRadians(angle - 180.0f))
                        val z = -cos(toRadians(angle - 180.0f))
                        val vec = Vector3f(x, 0.0f, z)

                        payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
                    }
                }
                InputHandler.KEY_A -> {
                    PlayerDirectionChangeNetworkEvent().apply {
                        val angle = state.player!!.rotation.y - 10.0f

                        val x = sin(toRadians(angle - 180.0f))
                        val z = -cos(toRadians(angle - 180.0f))
                        val vec = Vector3f(x, 0.0f, z)

                        payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
                    }
                }
                InputHandler.KEY_SPACE -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(0.0f)
                    }
                }
                InputHandler.KEY_P -> {
                    PlayerShootNetworkEvent()
                }
                else -> {
                    null
                }
            }

            if (networkEvent != null) {
                when (networkEvent) {
                    is PlayerDirectionChangeNetworkEvent -> {
                        window.setTimeout({
                            state.player?.velocity = object : IVector3f {
                                override var x: Float = networkEvent.payload!!.x
                                override var y: Float = networkEvent.payload!!.y
                                override var z: Float = networkEvent.payload!!.z
                            }
                        }, (networkClient.pingToServer / 2f).roundToInt())
                        networkClient.sendEvent(networkEvent, PlayerDirectionChangeNetworkEvent::class.serializer())
                    }
                    is PlayerSpeedChangeNetworkEvent -> {
                        networkClient.sendEvent(networkEvent, PlayerSpeedChangeNetworkEvent::class.serializer())
                    }
                    is PlayerShootNetworkEvent -> {
                        networkClient.sendEvent(networkEvent, PlayerShootNetworkEvent::class.serializer())
                    }
                }
            }
        }
        window.addEventListener("keydown", eventListener)
    }

    fun update() {
        if (inputHandler.isGamepadConnected()) {
            networkClient.sendEvent(PlayerDirectionChangeNetworkEvent().apply {
                val angle = oldAngle + (inputHandler.getGamepad().axes[InputHandler.GAMEPAD_AXIS_MAIN_X].toFloat() * 2.0f)
                oldAngle = angle

                val x = sin(toRadians(angle - 180.0f))
                val z = -cos(toRadians(angle - 180.0f))
                val vec = Vector3f(x, 0.0f, z)

                payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
            }, PlayerDirectionChangeNetworkEvent::class.serializer())

            networkClient.sendEvent(PlayerSpeedChangeNetworkEvent().apply {
                payload = PlayerSpeedChange(-inputHandler.getGamepad().axes[InputHandler.GAMEPAD_AXIS_MAIN_Y].toFloat() * 20.0f)
            }, PlayerSpeedChangeNetworkEvent::class.serializer())
        }
    }
}
