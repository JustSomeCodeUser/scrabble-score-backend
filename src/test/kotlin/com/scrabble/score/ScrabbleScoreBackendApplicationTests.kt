package com.scrabble.score

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.scrabble.score.service.ScoreService
import com.scrabble.score.web.models.request.WordInput
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ScrabbleScoreBackendApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var scoreService: ScoreService

    @Test
    fun contextLoads() {
    }

    @Test
    fun `test calculateScore endpoint`() {
        val wordInput = WordInput("hello")
        whenever(scoreService.calculateScore(any())).thenReturn(8)

        mockMvc.perform(post("/api/scores/calculate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jacksonObjectMapper().writeValueAsString(wordInput)))
            .andExpect(status().isOk)
            .andExpect(content().string("8"))
    }
}
