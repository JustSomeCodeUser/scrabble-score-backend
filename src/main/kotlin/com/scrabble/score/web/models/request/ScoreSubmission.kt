package com.scrabble.score.web.models.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class ScoreSubmission(
    @field:NotEmpty(message = "Player name cannot be empty")
    val playerName: String,
    @field:Valid
    val scores: List<ScoreInput>
)