package com.scrabble.score.repository

import com.scrabble.score.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface SessionRepository : JpaRepository<Session, UUID> {
    @Query("""
        SELECT s FROM Session s
        ORDER BY s.totalScore DESC, s.endTime ASC
    """)
    fun findTop10ByOrderByTotalScoreDescAndEndTimeAsc(): List<Session>
}
