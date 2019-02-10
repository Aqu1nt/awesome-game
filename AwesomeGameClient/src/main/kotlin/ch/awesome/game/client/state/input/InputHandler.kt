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

        val KEY_BACKSPACE = 8
        val KEY_TAB = 9
        val KEY_ENTER = 13
        val KEY_SHIFT = 16
        val KEY_CTRL = 17
        val KEY_ALT = 18
        val KEY_ESCAPE = 27
        val KEY_SPACE = 32
        val KEY_LEFT = 37
        val KEY_UP = 38
        val KEY_RIGHT = 39
        val KEY_DOWN = 40
        val KEY_0 = 48
        val KEY_1 = 49
        val KEY_2 = 50
        val KEY_3 = 51
        val KEY_4 = 52
        val KEY_5 = 53
        val KEY_6 = 54
        val KEY_7 = 55
        val KEY_8 = 56
        val KEY_9 = 57
        val KEY_A = 65
        val KEY_B = 66
        val KEY_C = 67
        val KEY_D = 68
        val KEY_E = 69
        val KEY_F = 70
        val KEY_G = 71
        val KEY_H = 72
        val KEY_I = 73
        val KEY_J = 74
        val KEY_K = 75
        val KEY_L = 76
        val KEY_M = 77
        val KEY_N = 78
        val KEY_O = 79
        val KEY_P = 80
        val KEY_Q = 81
        val KEY_R = 82
        val KEY_S = 83
        val KEY_T = 84
        val KEY_U = 85
        val KEY_V = 86
        val KEY_W = 87
        val KEY_X = 88
        val KEY_Y = 89
        val KEY_Z = 90
        val KEY_F1 = 112
        val KEY_F2 = 113
        val KEY_F3 = 114
        val KEY_F4 = 115
        val KEY_F5 = 116
        val KEY_F6 = 117
        val KEY_F7 = 118
        val KEY_F8 = 119
        val KEY_F9 = 120
        val KEY_F10 = 121
        val KEY_F11 = 122
        val KEY_F12 = 123


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