package com.scrabble.score.controller

import com.scrabble.score.exception.SessionNotFoundException
import com.scrabble.score.service.ScoreService
import com.scrabble.score.web.models.request.ScoreSubmission
import com.scrabble.score.web.models.request.WordInput
import com.scrabble.score.web.models.response.SessionResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scores")
@Validated
class ScoreController(private val scoreService: ScoreService) {

    private val logger = LoggerFactory.getLogger(ScoreController::class.java)

    @PostMapping("/calculate")
    fun calculateScore(@Valid @RequestBody wordInput: WordInput): ResponseEntity<Int> {
        val score = scoreService.calculateScore(wordInput.word)
        return ResponseEntity.ok(score)
    }

    @PostMapping("/submit")
    fun submitScore(@Valid @RequestBody scoreSubmission: ScoreSubmission): ResponseEntity<String> {
        return try {
            val session = scoreService.createSession(scoreSubmission.playerName)
            scoreSubmission.scores.forEach {
                scoreService.saveScore(it.word, session)
            }
            scoreService.closeSession(session.sessionId)
            ResponseEntity.ok("Scores submitted successfully")
        } catch (e: SessionNotFoundException) {
            logger.error("Error submitting scores: ${e.message}")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            logger.error("Unexpected error: ${e.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred")
        }
    }

    @GetMapping("/top")
    fun getTopScores(): ResponseEntity<List<SessionResponse>> {
        val topScores = scoreService.getTopScores()
        return ResponseEntity.ok(topScores)
    }
}
