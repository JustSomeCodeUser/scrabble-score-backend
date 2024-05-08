package com.scrabble.score.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

@Entity
data class Score(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotEmpty(message = "Word cannot be empty")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "Word must contain only alphabetical characters")
        val word: String,

        val wordScore: Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "session_id")
        val session: Session
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Score) return false
                return id == other.id && word == other.word && wordScore == other.wordScore && session == other.session
        }

        override fun hashCode(): Int {
                return id.hashCode() + word.hashCode() + wordScore + session.hashCode()
        }
}