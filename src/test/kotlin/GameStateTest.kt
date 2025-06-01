import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GameStateTest {
    @Nested
    inner class KeepPlayingGame {
        @ParameterizedTest
        @CsvSource(
            "should keep playing when score is tied after top of 1st, 1, true, 1, 1, true",
            "should keep playing when home team is behind after top of 1st, 1, true, 1, 0, true",
            "should keep playing when home team is ahead after top of 1st, 1, true, 0, 1, true",
            "should keep playing when score is tied after bottom of 1st, 1, false, 1, 1, true",
            "should keep playing when home team is behind after bottom of 1st, 1, false, 1, 0, true",
            "should keep playing when home team is ahead after bottom of 1st, 1, false, 0, 1, true",
            "should keep playing when score is tied after top of 9th, 9, true, 1, 1, true",
            "should keep playing when home team is behind after top of 9th, 9, true, 1, 0, true",
            "should end game when home team is ahead after top of 9th, 9, true, 0, 1, false",
            "should keep playing when score is tied after bottom of 9th, 9, false, 1, 1, true",
            "should end game when home team is behind after bottom of 9th, 9, false, 1, 0, false",
            "should end game when home team is ahead after bottom of 9th, 9, false, 0, 1, false",
            "should keep playing when score is tied after top of 10th, 10, true, 1, 1, true",
            "should keep playing when home team is behind after top of 10th, 10, true, 1, 0, true",
            "should end game when home team is ahead after top of 10th, 10, true, 0, 1, false",
            "should keep playing when score is tied after bottom of 10th, 10, false, 1, 1, true",
            "should end game when home team is behind after bottom of 10th, 10, false, 1, 0, false",
            "should end game when home team is ahead after bottom of 10th, 10, false, 0, 1, false",
        )
        fun `returns correct value for each game state`(description: String, inning: Int, isTopInning: Boolean, awayScore: Int, homeScore: Int, expected: Boolean) {
            val gameState = GameState(
                inning = inning,
                isTopInning = isTopInning,
                outs = 3,
                awayScore = awayScore,
                homeScore = homeScore,
            )

            val actual = gameState.keepPlayingGame()

            assertEquals(expected, actual, description)
        }
    }

    @Nested
    inner class KeepPlayingHalfInning {
        @ParameterizedTest
        @CsvSource(
            "should keep playing when no outs in top of 1st, 1, true, 0, 0, 0, true",
            "should keep playing when one out in top of 1st, 1, true, 1, 0, 0, true",
            "should keep playing when two outs in top of 1st, 1, true, 2, 0, 0, true",
            "should end inning when three outs in top of 1st, 1, true, 3, 0, 0, false",
            "should keep playing when no outs in bottom of 1st, 1, false, 0, 0, 0, true",
            "should keep playing when one out in bottom of 1st, 1, false, 1, 0, 0, true",
            "should keep playing when two outs in bottom of 1st, 1, false, 2, 0, 0, true",
            "should end inning when three outs in bottom of 1st, 1, false, 3, 0, 0, false",
            "should keep playing when no outs in top of 9th, 9, true, 0, 0, 0, true",
            "should keep playing when one out in top of 9th, 9, true, 1, 0, 0, true",
            "should keep playing when two outs in top of 9th, 9, true, 2, 0, 0, true",
            "should end inning when three outs in top of 9th, 9, true, 3, 0, 0, false",
            "should keep playing when time game with no outs in bottom of 9th, 9, false, 0, 0, 0, true",
            "should keep playing when time game with one out in bottom of 9th, 9, false, 1, 0, 0, true",
            "should keep playing when time game with two outs in bottom of 9th, 9, false, 2, 0, 0, true",
            "should end inning when time game with three outs in bottom of 9th, 9, false, 3, 0, 0, false",
            "should keep playing when home team is behind with no outs in bottom of 9th, 9, false, 0, 1, 0, true",
            "should keep playing when home team is behind with one out in bottom of 9th, 9, false, 1, 1, 0, true",
            "should keep playing when home team is behind with two outs in bottom of 9th, 9, false, 2, 1, 0, true",
            "should end inning when home team is behind with three outs in bottom of 9th, 9, false, 3, 1, 0, false",
            "should end inning when home team is ahead with no outs in bottom of 9th, 9, false, 0, 0, 1, false",
            "should end inning when home team is ahead with one out in bottom of 9th, 9, false, 1, 0, 1, false",
            "should end inning when home team is ahead with two outs in bottom of 9th, 9, false, 2, 0, 1, false",
            "should end inning when home team is ahead with three outs in bottom of 9th, 9, false, 3, 0, 1, false",
        )
        fun `returns correct value for each game state`(description: String, inning: Int, isTopInning: Boolean, outs: Int, awayScore: Int, homeScore: Int, expected: Boolean) {
            val gameState = GameState(
                inning = inning,
                isTopInning = isTopInning,
                outs = outs,
                awayScore = awayScore,
                homeScore = homeScore,
            )

            val actual = gameState.keepPlayingHalfInning()

            assertEquals(expected, actual, description)
        }
    }

    @Nested
    inner class NextHalfInning {
        @ParameterizedTest
        @CsvSource(
            // description, initialInning, initialIsTopInning, initialOuts, initialFirstBase, initialSecondBase, initialThirdBase, initialAwayScore, initialHomeScore, expectedInning, expectedIsTopInning
            "should clear bases and reset outs, 1, true, 2, true, true, false, 0, 0, 1, false",
            "should toggle from top to bottom of inning without incrementing inning number, 3, true, 3, false, false, false, 2, 1, 3, false",
            "should toggle from bottom to top of inning and increment inning number, 5, false, 3, false, false, false, 3, 3, 6, true"
        )
        fun `should handle next half inning correctly`(
            description: String,
            initialInning: Int,
            initialIsTopInning: Boolean,
            initialOuts: Int,
            initialFirstBase: Boolean,
            initialSecondBase: Boolean,
            initialThirdBase: Boolean,
            initialAwayScore: Int,
            initialHomeScore: Int,
            expectedInning: Int,
            expectedIsTopInning: Boolean
        ) {
            // Arrange
            val initialBases = booleanArrayOf(initialFirstBase, initialSecondBase, initialThirdBase)
            val gameState = GameState(
                inning = initialInning,
                isTopInning = initialIsTopInning,
                outs = initialOuts,
                awayScore = initialAwayScore,
                homeScore = initialHomeScore,
                bases = initialBases
            )

            // Act
            gameState.nextHalfInning()

            // Assert
            assertEquals(expectedInning, gameState.inning, "$description: Inning should be correct")
            assertEquals(expectedIsTopInning, gameState.isTopInning, "$description: isTopInning should be correct")
            assertFalse(gameState.firstBase(), "$description: First base should be empty")
            assertFalse(gameState.secondBase(), "$description: Second base should be empty")
            assertFalse(gameState.thirdBase(), "$description: Third base should be empty")
            assertEquals(0, gameState.outs, "$description: Outs should be reset to 0")
        }
    }

    @Nested
    inner class ProcessEvent {
        @ParameterizedTest
        @CsvSource(
            // description, initialInning, initialIsTopInning, initialOuts, initialFirstBase, initialSecondBase, initialThirdBase, initialAwayScore, initialHomeScore, eventType, eventFirstBase, eventSecondBase, eventThirdBase, eventRunsScored, eventOutsAdded, expectedFirstBase, expectedSecondBase, expectedThirdBase, expectedAwayScore, expectedHomeScore, expectedOuts
            "should update base state from event, 2, true, 1, true, false, true, 3, 2, SINGLE, false, true, true, 0, 0, false, true, true, 3, 2, 1",
            "should increment away score when runs scored in top of inning, 3, true, 0, false, false, false, 5, 4, HOME_RUN, false, false, false, 4, 0, false, false, false, 9, 4, 0",
            "should increment home score when runs scored in bottom of inning, 5, false, 2, true, true, true, 7, 6, TRIPLE, false, false, true, 3, 0, false, false, true, 7, 9, 2",
            "should increment outs by outsAdded from event, 7, true, 1, true, false, false, 2, 2, GROUND_OUT, false, false, false, 0, 1, false, false, false, 2, 2, 2",
            "should handle double play with two outs added, 4, false, 0, true, true, false, 3, 1, GROUND_OUT, false, false, false, 0, 2, false, false, false, 3, 1, 2",
            "should handle sacrifice fly with run scored and out added, 6, true, 1, false, false, true, 4, 4, FLY_OUT, false, false, false, 1, 1, false, false, false, 5, 4, 2",
            "should handle complex event with multiple changes, 9, false, 0, true, true, true, 8, 6, DOUBLE, false, true, false, 2, 1, false, true, false, 8, 8, 1",
            "should handle event with no changes, 1, true, 0, false, false, false, 0, 0, POP_OUT, false, false, false, 0, 0, false, false, false, 0, 0, 0",
            "should handle event with maximum possible runs, 3, true, 0, true, true, true, 10, 5, HOME_RUN, false, false, false, 4, 0, false, false, false, 14, 5, 0"
        )
        fun `should process events correctly`(
            description: String,
            initialInning: Int,
            initialIsTopInning: Boolean,
            initialOuts: Int,
            initialFirstBase: Boolean,
            initialSecondBase: Boolean,
            initialThirdBase: Boolean,
            initialAwayScore: Int,
            initialHomeScore: Int,
            eventType: Event.Type,
            eventFirstBase: Boolean,
            eventSecondBase: Boolean,
            eventThirdBase: Boolean,
            eventRunsScored: Int,
            eventOutsAdded: Int,
            expectedFirstBase: Boolean,
            expectedSecondBase: Boolean,
            expectedThirdBase: Boolean,
            expectedAwayScore: Int,
            expectedHomeScore: Int,
            expectedOuts: Int,
        ) {
            // Arrange
            val initialBases = booleanArrayOf(initialFirstBase, initialSecondBase, initialThirdBase)
            val gameState = GameState(
                inning = initialInning,
                isTopInning = initialIsTopInning,
                outs = initialOuts,
                awayScore = initialAwayScore,
                homeScore = initialHomeScore,
                bases = initialBases
            )
            val event = Event(
                type = eventType,
                bases = booleanArrayOf(eventFirstBase, eventSecondBase, eventThirdBase),
                runsScored = eventRunsScored,
                outsAdded = eventOutsAdded
            )

            // Act
            gameState.processEvent(event)

            // Assert
            assertEquals(expectedFirstBase, gameState.firstBase(), "$description: First base state incorrect")
            assertEquals(expectedSecondBase, gameState.secondBase(), "$description: Second base state incorrect")
            assertEquals(expectedThirdBase, gameState.thirdBase(), "$description: Third base state incorrect")
            assertEquals(expectedAwayScore, gameState.awayScore, "$description: Away score incorrect")
            assertEquals(expectedHomeScore, gameState.homeScore, "$description: Home score incorrect")
            assertEquals(expectedOuts, gameState.outs, "$description: Outs incorrect")
        }
    }
}
