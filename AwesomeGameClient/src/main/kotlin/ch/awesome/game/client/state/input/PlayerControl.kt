package ch.awesome.game.client.state.input

import ch.awesome.game.client.networking.NetworkClient
import ch.awesome.game.client.state.GameState
import ch.awesome.game.common.math.IVector3f
import ch.awesome.game.common.math.Vector3f
import ch.awesome.game.common.math.toDegrees
import ch.awesome.game.common.math.toRadians
import ch.awesome.game.common.network.events.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.serializer
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.js.Date
import kotlin.math.*

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
                InputHandler.KEY_SHIFT -> {
                    PlayerSpeedChangeNetworkEvent().apply {
                        payload = PlayerSpeedChange(0.0f)
                    }
                }
                InputHandler.KEY_SPACE -> {
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

    val useGamepadTimer = Date.now()

    fun update() {
        if (Date.now() >= useGamepadTimer + 1000 && inputHandler.isGamepadConnected()) {
            val rawX = -inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_MAIN_X).toFloat()
            val rawZ = -inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_MAIN_Y).toFloat()

            if(rawX != 0.0f || rawZ != 0.0f) {
                networkClient.sendEvent(PlayerDirectionChangeNetworkEvent().apply {
                    val theta = atan2(rawZ, rawX) + (PI.toFloat() / 2.0f)
                    var angle = toDegrees(theta)
                    angle += state.camera.yaw - 180.0f
                    if (angle < 0) angle += 360.0f

                    var vec = Vector3f(sin(toRadians(angle)), 0.0f, -cos(toRadians(angle)))
                    if (angle == 90.0f) vec = Vector3f(1.0f, 0.0f, 0.0f)
                    else if (angle == 180.0f) vec = Vector3f(0.0f, 0.0f, 1.0f)
                    else if (angle == 270.0f) vec = Vector3f(-1.0f, 0.0f, 0.0f)

                    payload = PlayerDirectionChange(x = vec.x, y = vec.y, z = vec.z)
                }, PlayerDirectionChangeNetworkEvent::class.serializer())
            }

            networkClient.sendEvent(PlayerSpeedChangeNetworkEvent().apply {
                console.log(inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_MAIN_X),
                            inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_MAIN_Y))

                var speed = 0.0f
                if (inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_MAIN_X).toFloat() != 0.0f ||
                    inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_MAIN_Y).toFloat() != 0.0f) speed = 20.0f
                payload = PlayerSpeedChange(speed)
            }, PlayerSpeedChangeNetworkEvent::class.serializer())

            state.camera.angleAround += inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_SECOND_X).toFloat()
            state.camera.pitch -= inputHandler.getGamepadAxis(InputHandler.GAMEPAD_AXIS_SECOND_Y).toFloat()
        }
    }
}
