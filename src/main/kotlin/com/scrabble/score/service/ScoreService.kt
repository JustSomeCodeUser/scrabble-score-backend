package com.scrabble.score.service

import com.scrabble.score.model.Score
import com.scrabble.score.model.Session
import com.scrabble.score.web.models.response.SessionResponse
import java.util.*

interface ScoreService {
    fun calculateScore(word: String): Int
    fun saveScore(word: String, session: Session): Score
    fun createSession(playerName: String): Session
    fun closeSession(sessionId: UUID): Session
    fun getTopScores(): List<SessionResponse>
}
