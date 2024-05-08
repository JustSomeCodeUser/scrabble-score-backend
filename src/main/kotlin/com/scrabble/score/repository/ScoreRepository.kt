package com.scrabble.score.repository

import com.scrabble.score.model.Score
import org.springframework.data.jpa.repository.JpaRepository

interface ScoreRepository : JpaRepository<Score, Long>