import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DefaultRunnerAdvancementCalculatorTest {

    @Nested
    inner class CalculateAdvancement {

        @ParameterizedTest
        @CsvSource(
            // description, eventType, initialFirstBase, initialSecondBase, initialThirdBase, initialOuts, expectedFirstBase, expectedSecondBase, expectedThirdBase, expectedRunsScored, expectedOutsAdded
            // Hit events
            "should calculate SINGLE event correctly, SINGLE, false, false, false, 0, true, false, false, 0, 0",
            "should calculate SINGLE event with runners correctly, SINGLE, true, true, true, 0, true, true, false, 2, 0",
            "should calculate DOUBLE event correctly, DOUBLE, false, false, false, 0, false, true, false, 0, 0",
            "should calculate DOUBLE event with runners correctly, DOUBLE, true, true, true, 0, false, true, true, 2, 0",
            "should calculate TRIPLE event correctly, TRIPLE, false, false, false, 0, false, false, true, 0, 0",
            "should calculate TRIPLE event with runners correctly, TRIPLE, true, true, true, 0, false, false, true, 3, 0",
            "should calculate HOME_RUN event correctly, HOME_RUN, false, false, false, 0, false, false, false, 1, 0",
            "should calculate HOME_RUN event with runners correctly, HOME_RUN, true, true, true, 0, false, false, false, 4, 0",

            // Walk events
            "should calculate WALK event correctly, WALK, false, false, false, 0, true, false, false, 0, 0",
            "should calculate WALK event with bases loaded correctly, WALK, true, true, true, 0, true, true, true, 1, 0",
            "should calculate WALK event with runners on first and second correctly, WALK, true, true, false, 0, true, true, true, 0, 0",

            // Ground out events
            "should calculate GROUND_OUT event with no runners correctly with 2 outs (single out), GROUND_OUT, false, false, false, 2, false, false, false, 0, 1",
            "should calculate GROUND_OUT event with runner on first correctly with less than 2 outs (double play), GROUND_OUT, true, false, false, 0, false, false, false, 0, 2",
            "should calculate GROUND_OUT event with runner on first correctly with 2 outs (single out), GROUND_OUT, true, false, false, 2, true, false, false, 0, 1",
            "should calculate GROUND_OUT event with no runners correctly, GROUND_OUT, false, false, false, 0, false, false, false, 0, 1",
            "should calculate GROUND_OUT event with runners on first and second correctly with less than 2 outs (double play), GROUND_OUT, true, true, false, 0, true, false, false, 0, 2",
            "should calculate GROUND_OUT event with runners on first and second correctly with 2 outs (single out), GROUND_OUT, true, true, false, 2, true, true, false, 0, 1",
            "should calculate GROUND_OUT event with bases loaded correctly with less than 2 outs (double play), GROUND_OUT, true, true, true, 0, true, true, false, 0, 2",
            "should calculate GROUND_OUT event with bases loaded correctly with 2 outs (single out), GROUND_OUT, true, true, true, 2, true, true, true, 0, 1",

            // Standard out events
            "should calculate FLY_OUT event correctly, FLY_OUT, true, false, false, 0, true, false, false, 0, 1",
            "should calculate LINE_OUT event correctly, LINE_OUT, true, false, false, 0, true, false, false, 0, 1",
            "should calculate POP_OUT event correctly, POP_OUT, true, false, false, 0, true, false, false, 0, 1",

            // Base stealing events
            "should calculate STOLEN_BASE event correctly, STOLEN_BASE, true, false, false, 0, false, true, false, 0, 0",
            "should calculate CAUGHT_STEALING event correctly, CAUGHT_STEALING, true, false, false, 0, false, false, false, 0, 1",

            // Strike out event
            "should calculate STRIKE_OUT event correctly, STRIKE_OUT, true, false, false, 0, true, false, false, 0, 1",

            // Sacrifice fly scenarios
            "should calculate sacrifice fly correctly with runner on third and less than 2 outs, FLY_OUT, false, false, true, 1, false, false, false, 1, 1",
            "should not score on fly out with 2 outs and runner on third, FLY_OUT, false, false, true, 2, false, false, true, 0, 1"
        )
        fun `should calculate all events correctly`(
            description: String,
            eventType: Event.Type,
            initialFirstBase: Boolean,
            initialSecondBase: Boolean,
            initialThirdBase: Boolean,
            initialOuts: Int,
            expectedFirstBase: Boolean,
            expectedSecondBase: Boolean,
            expectedThirdBase: Boolean,
            expectedRunsScored: Int,
            expectedOutsAdded: Int
        ) {
            // Arrange
            val initialBases = booleanArrayOf(initialFirstBase, initialSecondBase, initialThirdBase)
            val gameState = GameState(
                inning = 1,
                isTopInning = true,
                outs = initialOuts,
                awayScore = 0,
                homeScore = 0,
                bases = initialBases
            )
            val runnerAdvancementCalculator = DefaultRunnerAdvancementCalculator()

            // Act
            val (newBases, runsScored, outsAdded) = runnerAdvancementCalculator.calculateAdvancement(gameState, eventType)

            // Assert
            assertEquals(expectedFirstBase, newBases[0], "$description: First base state incorrect")
            assertEquals(expectedSecondBase, newBases[1], "$description: Second base state incorrect")
            assertEquals(expectedThirdBase, newBases[2], "$description: Third base state incorrect")
            assertEquals(expectedRunsScored, runsScored, "$description: Runs scored incorrect")
            assertEquals(expectedOutsAdded, outsAdded, "$description: Outs added incorrect")
        }
    }
}
