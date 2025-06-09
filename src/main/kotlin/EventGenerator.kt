interface EventGenerator {
    fun generateNextEvent(gameState: GameState): Event
}
