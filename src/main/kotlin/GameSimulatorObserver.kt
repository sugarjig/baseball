interface GameSimulatorObserver {
    /**
     * Called when there is a new game state to observe
     */
    fun onNewGameState(gameState: GameState)
    
    /**
     * Called when a new event occurs in the game
     */
    fun onNewEvent(event: Event)
    
    /**
     * Called when the game is over
     */
    fun onGameOver(gameState: GameState)
    
    /**
     * Called when a new half-inning starts
     */
    fun onNewHalfInning(gameState: GameState)
}
