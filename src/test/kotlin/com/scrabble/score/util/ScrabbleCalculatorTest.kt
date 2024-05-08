import com.scrabble.score.util.ScrabbleCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScrabbleCalculatorTest {

    @Test
    fun `test score calculation for single letters`() {
        assertEquals(1, ScrabbleCalculator.calculateScore("A"))
        assertEquals(3, ScrabbleCalculator.calculateScore("B"))
        assertEquals(10, ScrabbleCalculator.calculateScore("Z"))
        assertEquals(6, ScrabbleCalculator.calculateScore("K"))
    }

    @Test
    fun `test score calculation for words`() {
        assertEquals(8, ScrabbleCalculator.calculateScore("HELLO")) // H=4, E=1, L=1*2, O=1
        assertEquals(21, ScrabbleCalculator.calculateScore("QUICK")) // Q=10, U=1, I=1, C=3, K=6
    }

    @Test
    fun `test score calculation with mixed case input`() {
        assertEquals(8, ScrabbleCalculator.calculateScore("Hello"))
        assertEquals(21, ScrabbleCalculator.calculateScore("Quick"))
    }

    @Test
    fun `test score calculation with non-letter characters`() {
        assertEquals(8, ScrabbleCalculator.calculateScore("H-E-L-L-O"))
        assertEquals(21, ScrabbleCalculator.calculateScore("QUICK!"))
    }

    @Test
    fun `test score calculation for empty string`() {
        assertEquals(0, ScrabbleCalculator.calculateScore(""))
    }

    @Test
    fun `test score calculation for whitespace`() {
        assertEquals(0, ScrabbleCalculator.calculateScore(" "))
        assertEquals(0, ScrabbleCalculator.calculateScore("     "))
    }

    @Test
    fun `test score calculation with accented letters`() {
        assertEquals(0, ScrabbleCalculator.calculateScore("àèìòù"))
        assertEquals(8, ScrabbleCalculator.calculateScore("café"))
    }
}
