interface RunnerAdvancementCalculator {
    /**
     * Calculates the new base state, runs scored, and outs added after an event.
     *
     * @param gameState The current game state
     * @param eventType The type of event that occurred
     * @return The runner advancements
     */
    fun calculateAdvancements(gameState: GameState, eventType: Event.Type): List<RunnerAdvancement>
}
