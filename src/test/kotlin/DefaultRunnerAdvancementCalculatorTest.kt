import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DefaultRunnerAdvancementCalculatorTest {

    @Nested
    inner class CalculateAdvancement {

        @ParameterizedTest
        @CsvSource(
            // description, eventType, initialFirstBase, initialSecondBase, initialThirdBase, initialOuts, expectedBatterAdvancement, expectedBatterEndBase, expectedBatterIsOut, expectedRunnerFirstAdvancement, expectedRunnerFirstEndBase, expectedRunnerFirstIsOut, expectedRunnerSecondAdvancement, expectedRunnerSecondEndBase, expectedRunnerSecondIsOut, expectedRunnerThirdAdvancement, expectedRunnerThirdEndBase, expectedRunnerThirdIsOut
            // Hit events
            "should calculate SINGLE event correctly, SINGLE, false, false, false, 0, true, FIRST, false, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate SINGLE event with runners correctly, SINGLE, true, true, true, 0, true, FIRST, false, true, SECOND, false, true, HOME, false, true, HOME, false",
            "should calculate DOUBLE event correctly, DOUBLE, false, false, false, 0, true, SECOND, false, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate DOUBLE event with runners correctly, DOUBLE, true, true, true, 0, true, SECOND, false, true, THIRD, false, true, HOME, false, true, HOME, false",
            "should calculate TRIPLE event correctly, TRIPLE, false, false, false, 0, true, THIRD, false, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate TRIPLE event with runners correctly, TRIPLE, true, true, true, 0, true, THIRD, false, true, HOME, false, true, HOME, false, true, HOME, false",
            "should calculate HOME_RUN event correctly, HOME_RUN, false, false, false, 0, true, HOME, false, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate HOME_RUN event with runners correctly, HOME_RUN, true, true, true, 0, true, HOME, false, true, HOME, false, true, HOME, false, true, HOME, false",

            // Walk events
            "should calculate WALK event correctly, WALK, false, false, false, 0, true, FIRST, false, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate WALK event with bases loaded correctly, WALK, true, true, true, 0, true, FIRST, false, true, SECOND, false, true, THIRD, false, true, HOME, false",
            "should calculate WALK event with runners on first and second correctly, WALK, true, true, false, 0, true, FIRST, false, true, SECOND, false, true, THIRD, false, false, BATTER, false",

            // Ground out events
            "should calculate GROUND_OUT event with no runners correctly with 2 outs (single out), GROUND_OUT, false, false, false, 2, true, FIRST, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate GROUND_OUT event with runner on first correctly with less than 2 outs (double play), GROUND_OUT, true, false, false, 0, true, FIRST, true, true, SECOND, true, false, BATTER, false, false, BATTER, false",
            "should calculate GROUND_OUT event with runner on first correctly with 2 outs (single out), GROUND_OUT, true, false, false, 2, true, FIRST, false, true, SECOND, true, false, BATTER, false, false, BATTER, false",
            "should calculate GROUND_OUT event with no runners correctly, GROUND_OUT, false, false, false, 0, true, FIRST, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate GROUND_OUT event with runners on first and second correctly with less than 2 outs (double play), GROUND_OUT, true, true, false, 0, true, FIRST, false, true, SECOND, true, true, THIRD, true, false, BATTER, false",
            "should calculate GROUND_OUT event with runners on first and second correctly with 2 outs (single out), GROUND_OUT, true, true, false, 2, true, FIRST, false, true, SECOND, false, true, THIRD, true, false, BATTER, false",
            "should calculate GROUND_OUT event with bases loaded correctly with less than 2 outs (double play), GROUND_OUT, true, true, true, 0, true, FIRST, false, true, SECOND, false, true, THIRD, true, true, HOME, true",
            "should calculate GROUND_OUT event with bases loaded correctly with 2 outs (single out), GROUND_OUT, true, true, true, 2, true, FIRST, false, true, SECOND, false, true, THIRD, false, true, HOME, true",

            // Standard out events
            "should calculate FLY_OUT event correctly, FLY_OUT, true, false, false, 0, true, BATTER, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate LINE_OUT event correctly, LINE_OUT, true, false, false, 0, true, BATTER, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",
            "should calculate POP_OUT event correctly, POP_OUT, true, false, false, 0, true, BATTER, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",

            // Base stealing events
            "should calculate STOLEN_BASE event correctly, STOLEN_BASE, true, false, false, 0, false, BATTER, false, true, SECOND, false, false, BATTER, false, false, BATTER, false",
            "should calculate CAUGHT_STEALING event correctly, CAUGHT_STEALING, true, false, false, 0, false, BATTER, false, true, SECOND, true, false, BATTER, false, false, BATTER, false",

            // Strike out event
            "should calculate STRIKE_OUT event correctly, STRIKE_OUT, true, false, false, 0, true, BATTER, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",

            // Sacrifice fly scenarios
            "should calculate sacrifice fly correctly with runner on third and less than 2 outs, FLY_OUT, false, false, true, 1, true, BATTER, true, false, BATTER, false, false, BATTER, false, true, HOME, false",
            "should not score on fly out with 2 outs and runner on third, FLY_OUT, false, false, true, 2, true, BATTER, true, false, BATTER, false, false, BATTER, false, false, BATTER, false",
        )
        fun `should calculate all events correctly`(
            description: String,
            eventType: Event.Type,
            initialFirstBase: Boolean,
            initialSecondBase: Boolean,
            initialThirdBase: Boolean,
            initialOuts: Int,
            expectedBatterAdvancement: Boolean,
            expectedBatterEndBase: RunnerAdvancement.Base,
            expectedBatterIsOut: Boolean,
            expectedRunnerFirstAdvancement: Boolean,
            expectedRunnerFirstEndBase: RunnerAdvancement.Base,
            expectedRunnerFirstIsOut: Boolean,
            expectedRunnerSecondAdvancement: Boolean,
            expectedRunnerSecondEndBase: RunnerAdvancement.Base,
            expectedRunnerSecondIsOut: Boolean,
            expectedRunnerThirdAdvancement: Boolean,
            expectedRunnerThirdEndBase: RunnerAdvancement.Base,
            expectedRunnerThirdIsOut: Boolean,
        ) {
            // Arrange
            val gameState = GameState(
                inning = 1,
                isTopInning = true,
                outs = initialOuts,
                awayScore = 0,
                homeScore = 0,
                bases = booleanArrayOf(initialFirstBase, initialSecondBase, initialThirdBase)
            )
            val runnerAdvancementCalculator = DefaultRunnerAdvancementCalculator()

            // Act
            val runnerAdvancements = runnerAdvancementCalculator.calculateAdvancements(gameState, eventType)

            // Assert
            val expectedRunnerAdvancementsUnchecked = arrayOf(
                expectedBatterAdvancement,
                expectedRunnerFirstAdvancement,
                expectedRunnerSecondAdvancement,
                expectedRunnerThirdAdvancement,
            )
            runnerAdvancements.forEach {
                if (it.startBase == RunnerAdvancement.Base.BATTER) {
                    if (expectedBatterAdvancement) {
                        assertEquals(expectedBatterEndBase, it.endBase, "$description: Batter end base incorrect")
                        assertEquals(expectedBatterIsOut, it.isOut, "$description: Batter is out incorrect")
                    } else {
                        fail("$description: No batter advancement expected")
                    }
                    expectedRunnerAdvancementsUnchecked[0] = false
                }

                if (it.startBase == RunnerAdvancement.Base.FIRST) {
                    if (expectedRunnerFirstAdvancement) {
                        assertEquals(expectedRunnerFirstEndBase, it.endBase, "$description: Runner on first end base incorrect")
                        assertEquals(expectedRunnerFirstIsOut, it.isOut, "$description: Runner on first is out incorrect")
                    } else {
                        fail("$description: No runner on first advancement expected")
                    }
                    expectedRunnerAdvancementsUnchecked[1] = false
                }

                if (it.startBase == RunnerAdvancement.Base.SECOND) {
                    if (expectedRunnerSecondAdvancement) {
                        assertEquals(expectedRunnerSecondEndBase, it.endBase, "$description: Runner on second end base incorrect")
                        assertEquals(expectedRunnerSecondIsOut, it.isOut, "$description: Runner on second is out incorrect")
                    } else {
                        fail("$description: No runner on second advancement expected")
                    }
                    expectedRunnerAdvancementsUnchecked[2] = false
                }

                if (it.startBase == RunnerAdvancement.Base.THIRD) {
                    if (expectedRunnerThirdAdvancement) {
                        assertEquals(expectedRunnerThirdEndBase, it.endBase, "$description: Runner on third end base incorrect")
                        assertEquals(expectedRunnerThirdIsOut, it.isOut, "$description: Runner on third is out incorrect")
                    } else {
                        fail("$description: No runner on third advancement expected")
                    }
                    expectedRunnerAdvancementsUnchecked[3] = false
                }
            }

            assertFalse(expectedRunnerAdvancementsUnchecked.any { it }, "$description: Some expected runner advancements unchecked")
        }
    }
}
