interface RunnerAdvancementCalculator {
    /**
     * Calculates the new base state, runs scored, and outs added after an event.
     *
     * @param gameState The current game state
     * @param eventType The type of event that occurred
     * @return A triple containing:
     *         - The new base state as a BooleanArray (first, second, third)
     *         - The number of runs scored
     *         - The number of outs added
     */
    fun calculateAdvancement(gameState: GameState, eventType: Event.Type): Triple<BooleanArray, Int, Int>
}
