package ch.awesome.game.client.state.input

interface InputListener {

    fun mousePressed(button: Int)
    fun mouseReleased(button: Int)
    fun mouseMoved(x: Int, y: Int)
    fun mouseWheel(delta: Double)

    fun keyPressed(key: Int)
    fun keyReleased(key: Int)

    fun gamepadButtonPressed(button: Int)
    fun gamepadButtonReleased(button: Int)
    fun gamepadAxisChanged(axis: Int, value: Double)
}