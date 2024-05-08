package com.scrabble.score.web.models.request

import jakarta.validation.constraints.Pattern

data class WordInput(
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Word must contain only alphabetical characters")
    val word: String
)
