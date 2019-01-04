package ch.awesome.game

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

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