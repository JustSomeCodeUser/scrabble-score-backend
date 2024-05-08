package com.scrabble.score.web.models.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class ScoreInput(
    @field:NotEmpty(message = "Word cannot be empty")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Word must contain only alphabetical characters")
    val word: String,
    val score: Int
)