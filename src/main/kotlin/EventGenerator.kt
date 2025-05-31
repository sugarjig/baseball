class EventGenerator(
    private val randomGenerator: RandomGenerator,
    private val runnerAdvancementCalculator: RunnerAdvancementCalculator
) {
    fun generateNextEvent(gameState: GameState): Event {
        // Check for stolen base attempt
        if (gameState.firstBase() && !gameState.secondBase() && randomGenerator.nextDouble() < 0.25) {
            val stealSuccess = randomGenerator.nextDouble() < 0.5
            val eventType = if (stealSuccess) Event.Type.STOLEN_BASE else Event.Type.CAUGHT_STEALING
            val (bases, runsScored, outsAdded) = runnerAdvancementCalculator.calculateAdvancement(gameState, eventType)
            return Event(
                type = eventType,
                bases = bases,
                runsScored = runsScored,
                outsAdded = outsAdded
            )
        }

        // Regular event generation
        val random = randomGenerator.nextDouble()
        val eventType = when {
            random < 0.15 -> Event.Type.SINGLE
            random < 0.22 -> Event.Type.DOUBLE
            random < 0.24 -> Event.Type.TRIPLE
            random < 0.27 -> Event.Type.HOME_RUN
            random < 0.35 -> Event.Type.WALK
            random < 0.67 -> {
                // Randomly select one of the out types
                val outRandom = randomGenerator.nextDouble()
                when {
                    outRandom < 0.25 -> Event.Type.GROUND_OUT
                    outRandom < 0.50 -> Event.Type.FLY_OUT
                    outRandom < 0.75 -> Event.Type.LINE_OUT
                    else -> Event.Type.POP_OUT
                }
            }
            else -> Event.Type.STRIKE_OUT
        }

        val (bases, runsScored, outsAdded) = runnerAdvancementCalculator.calculateAdvancement(gameState, eventType)

        return Event(
            type = eventType,
            bases = bases,
            runsScored = runsScored,
            outsAdded = outsAdded
        )
    }
}
