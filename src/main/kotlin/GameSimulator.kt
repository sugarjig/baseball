class GameSimulator(
    private val eventGenerator: EventGenerator,
    private val observer: GameSimulatorObserver
) {
    fun simulateGame() {
        val gameState = GameState()

        playHalfInning(gameState)

        while (gameState.keepPlayingGame()) {
            gameState.nextHalfInning()
            playHalfInning(gameState)
        }

        observer.onGameOver(gameState)
    }

    private fun playHalfInning(gameState: GameState) {
        observer.onNewHalfInning(gameState)
        while (gameState.keepPlayingHalfInning()) {
            observer.onNewGameState(gameState)
            val event = eventGenerator.generateNextEvent(gameState)
            observer.onNewEvent(event)
            gameState.processEvent(event)
        }
    }
}
