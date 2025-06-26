class GameSimulatorPrinter : GameSimulatorObserver {
    override fun onNewGameState(gameState: GameState) {
        println(gameState)
    }
    
    override fun onNewEvent(event: Event) {
        println(event)
    }
    
    override fun onGameOver(gameState: GameState) {
        println("\nGame Over!")
        println(gameState)
    }
    
    override fun onNewHalfInning(gameState: GameState) {
        val halfInning = if (gameState.isTopInning) "Top" else "Bottom"
        val inningStr = when (gameState.inning) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            else -> "${gameState.inning}th"
        }
        println()
        println("$halfInning of the $inningStr")
    }
}
