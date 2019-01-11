package ch.awesome.game.server

import ch.awesome.game.common.math.Matrix4f
import ch.awesome.game.common.math.Vector3f
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

fun main(args: Array<String>) {

    val matrix = Matrix4f().identity()
    matrix.rotate(90f, Vector3f(0f, 1f, 0f))
    matrix.rotate(90f, Vector3f(1f, 0f, 0f))

    val position = Vector3f(10f, 0f, 10f)
    matrix.rotateVect(position)
    println(position)
}

@SpringBootApplication(exclude = [
    SecurityAutoConfiguration::class
])
class AwesomeGame {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AwesomeGame::class.java)
        }
    }
}