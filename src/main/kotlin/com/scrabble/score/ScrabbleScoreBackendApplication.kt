package com.scrabble.score

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScrabbleScoreBackendApplication

fun main(args: Array<String>) {
    runApplication<ScrabbleScoreBackendApplication>(*args)
}
