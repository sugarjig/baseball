private const val NUM_INNINGS_IN_GAME = 9

class GameState(
    var inning: Int = 1,
    var isTopInning: Boolean = true,
    var outs: Int = 0,
    var awayScore: Int = 0,
    var homeScore: Int = 0,
    bases: BooleanArray = BooleanArray(3) { false } // false = empty, true = runner present
) {
    private val bases: BooleanArray

    init {
        require(bases.size == 3) { "Bases array must contain exactly 3 elements" }
        this.bases = bases.copyOf()
    }

    fun firstBase(): Boolean {
        return bases[0]
    }

    fun secondBase(): Boolean {
        return bases[1]
    }

    fun thirdBase(): Boolean {
        return bases[2]
    }

    fun nextHalfInning() {
        // Clear the bases
        bases.fill(false)
        outs = 0

        if (!isTopInning) {
            inning++
        }
        isTopInning = !isTopInning
    }

    private fun scoreRun() {
        if (isTopInning) {
            awayScore++
        } else {
            homeScore++
        }
    }

    /**
     * Determines whether the game should continue based on the current game state. Intended to be called at the end of
     * a half-inning.
     *
     * @return true if the game should continue
     *         false if the game is over.
     */
    fun keepPlayingGame(): Boolean {
        return when {
            this.inning < NUM_INNINGS_IN_GAME -> true
            this.isTopInning && this.homeScore <= this.awayScore -> true
            !this.isTopInning && this.homeScore == this.awayScore -> true
            else -> false
        }
    }

    /**
     * Determines whether the current half-inning should continue based on the game's current state.
     *
     * @return true if the half-inning should continue
     *         false if the half-inning should end.
     */
    fun keepPlayingHalfInning(): Boolean {
        return when {
            this.inning < NUM_INNINGS_IN_GAME && outs < 3 -> true
            this.isTopInning && outs < 3 -> true
            !this.isTopInning && this.homeScore <= this.awayScore && outs < 3 -> true
            else -> false
        }
    }

    // Processes an event and updates the game state
    fun processEvent(event: Event) {
        // Update bases with the new state from the event
        for (i in 0..2) {
            this.bases[i] = event.bases[i]
        }

        // Add runs to score
        repeat(event.runsScored) {
            this.scoreRun()
        }

        // Add outs
        this.outs += event.outsAdded
    }

    override fun toString(): String {
        return "GameState(inning=$inning, isTopInning=$isTopInning, outs=$outs, awayScore=$awayScore, homeScore=$homeScore, bases=${bases.contentToString()})"
    }
}
