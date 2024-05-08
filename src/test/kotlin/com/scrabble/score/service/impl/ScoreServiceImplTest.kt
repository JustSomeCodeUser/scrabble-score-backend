package com.scrabble.score.service.impl

import com.scrabble.score.exception.SessionNotFoundException
import com.scrabble.score.model.Score
import com.scrabble.score.model.Session
import com.scrabble.score.repository.ScoreRepository
import com.scrabble.score.repository.SessionRepository
import com.scrabble.score.web.models.response.ScoreResponse
import com.scrabble.score.web.models.response.SessionResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ScoreServiceImplTest {

    @Mock
    private lateinit var scoreRepository: ScoreRepository

    @Mock
    private lateinit var sessionRepository: SessionRepository

    @InjectMocks
    private lateinit var scoreService: ScoreServiceImpl

    private lateinit var session1: Session
    private lateinit var session2: Session
    private lateinit var session3: Session

    @BeforeEach
    fun setup() {
        session1 = Session(
            sessionId = UUID.randomUUID(),
            startTime = LocalDateTime.now().minusDays(2),
            endTime = LocalDateTime.now().minusDays(1),
            totalScore = 30,
            playerName = "Player1"
        )
        session2 = Session(
            sessionId = UUID.randomUUID(),
            startTime = LocalDateTime.now().minusDays(3),
            endTime = LocalDateTime.now().minusDays(2),
            totalScore = 30,
            playerName = "Player2"
        )
        session3 = Session(
            sessionId = UUID.randomUUID(),
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now(),
            totalScore = 20,
            playerName = "Player3"
        )

        val score1 = Score(id = 1L, word = "HELLO", wordScore = 8, session = session1)
        val score2 = Score(id = 2L, word = "WORLD", wordScore = 12, session = session1)
        val score3 = Score(id = 3L, word = "GOOD", wordScore = 10, session = session2)
        val score4 = Score(id = 4L, word = "LUCK", wordScore = 5, session = session2)
        val score5 = Score(id = 5L, word = "NICE", wordScore = 20, session = session3)

        session1.scores.addAll(listOf(score1, score2))
        session2.scores.addAll(listOf(score3, score4))
        session3.scores.add(score5)

        lenient().`when`(sessionRepository.save(any())).thenAnswer { invocation ->
            invocation.getArgument<Session>(0)
        }

        lenient().`when`(scoreRepository.save(any())).thenAnswer { invocation ->
            val score = invocation.getArgument<Score>(0)
            score.copy(id = 99L)
        }
    }

    @Test
    fun `test calculateScore with uppercase conversion`() {
        assertEquals(8, scoreService.calculateScore("hello"))
        assertEquals(21, scoreService.calculateScore("quick"))
    }

    @Test
    fun `test saveScore with uppercase conversion`() {
        val session = Session(
            sessionId = UUID.randomUUID(),
            startTime = LocalDateTime.now(),
            endTime = null,
            totalScore = 0,
            playerName = "Player1"
        )

        whenever(sessionRepository.save(any<Session>())).thenReturn(session)

        val score = scoreService.saveScore("hello", session)
        assertEquals("HELLO", score.word)
        assertEquals(99L, score.id) // Ensuring the `copy` operation assigns a new ID
    }

    @Test
    fun `test createSession with player name`() {
        val session = Session(
            playerName = "Player1",
            startTime = LocalDateTime.now()
        )

        whenever(sessionRepository.save(any<Session>())).thenReturn(session)

        val newSession = scoreService.createSession("Player1")
        assertEquals("Player1", newSession.playerName)
    }

    @Test
    fun `test closeSession updates endTime`() {
        val session = Session(
            sessionId = UUID.randomUUID(),
            startTime = LocalDateTime.now(),
            endTime = null,
            totalScore = 0,
            playerName = "Player1"
        )

        whenever(sessionRepository.findById(any())).thenReturn(Optional.of(session))
        whenever(sessionRepository.save(any<Session>())).thenReturn(session)

        val closedSession = scoreService.closeSession(session.sessionId)
        assertNotNull(closedSession.endTime)
        assertEquals(session.sessionId, closedSession.sessionId)
        assertEquals(session.startTime, closedSession.startTime)
        assertEquals(session.endTime, closedSession.endTime)
    }

    @Test
    fun `test closeSession throws SessionNotFoundException`() {
        whenever(sessionRepository.findById(any())).thenReturn(Optional.empty())
        val sessionId = UUID.randomUUID()

        val exception = assertThrows<SessionNotFoundException> {
            scoreService.closeSession(sessionId)
        }
        assertEquals("Session with ID $sessionId not found", exception.message)
    }

    @Test
    fun `test getTopScores returns correct results`() {
        val expectedSessionResponse = listOf(
            SessionResponse(
                sessionId = session2.sessionId.toString(),
                startTime = session2.startTime,
                endTime = session2.endTime,
                totalScore = session2.totalScore,
                playerName = session2.playerName,
                scores = listOf(
                    ScoreResponse(word = "GOOD", wordScore = 10),
                    ScoreResponse(word = "LUCK", wordScore = 5)
                )
            ),
            SessionResponse(
                sessionId = session1.sessionId.toString(),
                startTime = session1.startTime,
                endTime = session1.endTime,
                totalScore = session1.totalScore,
                playerName = session1.playerName,
                scores = listOf(
                    ScoreResponse(word = "HELLO", wordScore = 8),
                    ScoreResponse(word = "WORLD", wordScore = 12)
                )
            ),
            SessionResponse(
                sessionId = session3.sessionId.toString(),
                startTime = session3.startTime,
                endTime = session3.endTime,
                totalScore = session3.totalScore,
                playerName = session3.playerName,
                scores = listOf(
                    ScoreResponse(word = "NICE", wordScore = 20)
                )
            )
        )

        whenever(sessionRepository.findTop10ByOrderByTotalScoreDescAndEndTimeAsc()).thenReturn(listOf(session2, session1, session3))
        val topScores = scoreService.getTopScores()
        assertEquals(expectedSessionResponse.size, topScores.size)
        assertEquals(expectedSessionResponse[0].playerName, topScores[0].playerName)
        assertEquals(expectedSessionResponse[0].totalScore, topScores[0].totalScore)
        assertEquals(expectedSessionResponse[0].scores.size, topScores[0].scores.size)
        assertEquals(expectedSessionResponse[1].playerName, topScores[1].playerName)
        assertEquals(expectedSessionResponse[2].totalScore, topScores[2].totalScore)
    }
}
