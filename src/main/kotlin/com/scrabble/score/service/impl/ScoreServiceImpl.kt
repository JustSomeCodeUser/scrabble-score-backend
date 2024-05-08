package com.scrabble.score.service.impl

import com.scrabble.score.exception.SessionNotFoundException
import com.scrabble.score.model.Score
import com.scrabble.score.model.Session
import com.scrabble.score.repository.ScoreRepository
import com.scrabble.score.repository.SessionRepository
import com.scrabble.score.service.ScoreService
import com.scrabble.score.util.ScrabbleCalculator
import com.scrabble.score.web.models.response.ScoreResponse
import com.scrabble.score.web.models.response.SessionResponse
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class ScoreServiceImpl(
    private val scoreRepository: ScoreRepository,
    private val sessionRepository: SessionRepository
) : ScoreService {

    private val logger = LoggerFactory.getLogger(ScoreServiceImpl::class.java)

    override fun calculateScore(word: String): Int {
        return ScrabbleCalculator.calculateScore(word.uppercase())
    }

    @Transactional
    override fun saveScore(word: String, session: Session): Score {
        val wordScore = calculateScore(word.uppercase())
        val score = Score(word = word.uppercase(), wordScore = wordScore, session = session)
        session.totalScore += wordScore
        sessionRepository.save(session)
        return scoreRepository.save(score)
    }

    @Transactional
    override fun createSession(playerName: String): Session {
        return sessionRepository.save(Session(playerName = playerName))
    }

    @Transactional
    override fun closeSession(sessionId: UUID): Session {
        val session = sessionRepository.findById(sessionId).orElseThrow {
            SessionNotFoundException("Session with ID $sessionId not found").also {
                logger.error(it.message)
            }
        }
        session.endTime = LocalDateTime.now()
        return sessionRepository.save(session)
    }

    @Cacheable("topScores")
    override fun getTopScores(): List<SessionResponse> {
        logger.info("Fetching top scores")
        return sessionRepository.findTop10ByOrderByTotalScoreDescAndEndTimeAsc().map { session ->
            SessionResponse(
                sessionId = session.sessionId.toString(),
                startTime = session.startTime,
                endTime = session.endTime,
                totalScore = session.totalScore,
                playerName = session.playerName,
                scores = session.scores.map { score ->
                    ScoreResponse(
                        word = score.word,
                        wordScore = score.wordScore
                    )
                }
            )
        }
    }
}
