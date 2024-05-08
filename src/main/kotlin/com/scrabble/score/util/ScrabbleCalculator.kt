package com.scrabble.score.util

object ScrabbleCalculator {
    private val letterScores = mapOf(
        'A' to 1, 'B' to 3, 'C' to 3, 'D' to 2, 'E' to 1, 'F' to 4, 'G' to 2, 'H' to 4, 'I' to 1,
        'J' to 8, 'K' to 6, 'L' to 1, 'M' to 3, 'N' to 1, 'O' to 1, 'P' to 3, 'Q' to 10, 'R' to 1,
        'S' to 1, 'T' to 1, 'U' to 1, 'V' to 4, 'W' to 4, 'X' to 8, 'Y' to 4, 'Z' to 10
    )

    fun calculateScore(word: String): Int = word.uppercase().filter { it in letterScores }.sumOf { char ->
        letterScores[char] ?: 0
    }
}