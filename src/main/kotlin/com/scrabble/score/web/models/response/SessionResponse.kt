package com.scrabble.score.web.models.response

import java.time.LocalDateTime

data class SessionResponse(
        val sessionId: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime?,
        val totalScore: Int,
        val playerName: String,
        val scores: List<ScoreResponse>
)
