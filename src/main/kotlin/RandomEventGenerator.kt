class RandomEventGenerator(
    private val eventTypeSampler: EventSampler,
    private val runnerAdvancementCalculator: RunnerAdvancementCalculator
) : EventGenerator {
    override fun generateNextEvent(gameState: GameState): Event {
        // Check for a stolen base attempt
        if (gameState.firstBase() && !gameState.secondBase() && eventTypeSampler.nextDouble() < 0.25) {
            val eventType = eventTypeSampler.sample(mapOf(
                Event.Type.STOLEN_BASE to 0.5,
                Event.Type.CAUGHT_STEALING to 0.5,
            ))
            val (bases, runsScored, outsAdded) = runnerAdvancementCalculator.calculateAdvancement(gameState, eventType)
            return Event(
                type = eventType,
                bases = bases,
                runsScored = runsScored,
                outsAdded = outsAdded
            )
        }

        val eventType = eventTypeSampler.sample(mapOf(
            Event.Type.SINGLE to 0.15,
            Event.Type.DOUBLE to 0.07,
            Event.Type.TRIPLE to 0.02,
            Event.Type.HOME_RUN to 0.03,
            Event.Type.WALK to 0.08,
            Event.Type.GROUND_OUT to 0.08,
            Event.Type.FLY_OUT to 0.08,
            Event.Type.LINE_OUT to 0.08,
            Event.Type.POP_OUT to 0.08,
            Event.Type.STRIKE_OUT to 0.33,
        ))

        val (bases, runsScored, outsAdded) = runnerAdvancementCalculator.calculateAdvancement(gameState, eventType)

        return Event(
            type = eventType,
            bases = bases,
            runsScored = runsScored,
            outsAdded = outsAdded
        )
    }
}
