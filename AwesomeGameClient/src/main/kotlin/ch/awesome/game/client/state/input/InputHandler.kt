package ch.awesome.game.client.state.input

import ch.awesome.game.client.lib.GameNavigator
import ch.awesome.game.client.lib.Gamepad
import ch.awesome.game.client.lib.GamepadEvent
import ch.awesome.game.client.lib.LockedMouseEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import kotlin.browser.window

class InputHandler {

    companion object {
        val MOUSE_BUTTON_LEFT = 0
        val MOUSE_BUTTON_MIDDLE = 1
        val MOUSE_BUTTON_RIGHT = 2

        val KEY_W = 87
        val KEY_A = 65
        val KEY_S = 83
        val KEY_D = 68
        val KEY_P = 80
        val KEY_SHIFT = 16
        val KEY_SPACE = 32

        val GAMEPAD_AXIS_MAIN_X = 0
        val GAMEPAD_AXIS_MAIN_Y = 1
        val GAMEPAD_AXIS_SECOND_X = 2
        val GAMEPAD_AXIS_SECOND_Y = 3

    }

    var mouseButtons = BooleanArray(3) {false}
    var keys = BooleanArray(300) {false}
    var gamepadButtons = BooleanArray(32) {false}
    var gamepadAxes = DoubleArray(3) {0.0}

    var gamepadConnected = false

    val listeners = arrayListOf<InputListener>()

    init {
        window.addEventListener("mousedown", { event ->
            val mouseEvent = event as MouseEvent
            val button = mouseEvent.button.toInt()

            if (!mouseButtons[button]) {
                mouseButtons[button] = true
                for (l in listeners) l.mousePressed(button)
            }
        })

        window.addEventListener("mouseup", { event ->
            val mouseEvent = event as MouseEvent
            val button = mouseEvent.button.toInt()

            if (mouseButtons[button]) {
                mouseButtons[button] = false
                for (l in listeners) l.mouseReleased(button)
            }
        })

        window.addEventListener("mousemove", { event ->
            val lockedMouseEvent = event as LockedMouseEvent
            for (l in listeners) l.mouseMoved(lockedMouseEvent.movementX, lockedMouseEvent.movementY)
        })

        window.addEventListener("wheel", { event ->
            val wheelEvent = event as WheelEvent
            for (l in listeners) l.mouseWheel(wheelEvent.deltaY)
        })

        window.addEventListener("keydown", { event ->
            val keyEvent = event as KeyboardEvent
            val key = keyEvent.keyCode

            if (!keys[key]) {
                keys[key] = true
                for (l in listeners) l.keyPressed(key)
            }
        })

        window.addEventListener("keyup", { event ->
            val keyEvent = event as KeyboardEvent
            val key = keyEvent.keyCode

            if (keys[key]) {
                keys[key] = false
                for (l in listeners) l.keyReleased(key)
            }
        })

        window.addEventListener("gamepadconnected", { event ->
            val gamepadEvent = event as GamepadEvent
            gamepadConnected = true
        })
    }

    fun update() {
        if (gamepadConnected) {
            for (i in 0 until getGamepad().buttons.size) {
                if(getGamepad().buttons[i].pressed) {
                    if(!gamepadButtons[i]) {
                        gamepadButtons[i] = true
                        for (l in listeners) l.gamepadButtonPressed(i)
                    }
                } else {
                   if(gamepadButtons[i]) {
                       gamepadButtons[i] = false
                       for (l in listeners) l.gamepadButtonReleased(i)
                   }
                }
            }

            for (i in 0 until getGamepad().axes.size) {
                if(getGamepad().axes[i] != gamepadAxes[i]) {
                    gamepadAxes[i] = getGamepad().axes[i]
                    for (l in listeners) l.gamepadAxisChanged(i, getGamepad().axes[i])
                }
            }
        }
    }

    fun isMouseButtonPressed(button: Int): Boolean {
        return mouseButtons[button]
    }

    fun isKeyPressed(key: Int): Boolean {
        return keys[key]
    }

    fun isGamepadConnected(): Boolean {
        return gamepadConnected && getGamepad().connected
    }

    fun getGamepad(): Gamepad {
        return (window.navigator as GameNavigator).getGamepads()[0]
    }

    fun getGamepadAxis(axis: Int): Double {
        val value = getGamepad().axes[axis]
        if (value > 0.004 || value < -0.004) return value
        else return 0.0
    }

    fun addInputListener(listener: InputListener) {
        listeners.add(listener)
    }

    fun removeInputListener(listener: InputListener) {
        listeners.remove(listener)
    }

}