package com.scrabble.score.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Session(
    @Id
    val sessionId: UUID = UUID.randomUUID(),
    val startTime: LocalDateTime = LocalDateTime.now(),
    var endTime: LocalDateTime? = null,
    var totalScore: Int = 0,
    val playerName: String = "",

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val scores: MutableList<Score> = mutableListOf()
)