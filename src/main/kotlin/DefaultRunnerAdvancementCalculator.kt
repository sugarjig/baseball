class DefaultRunnerAdvancementCalculator : RunnerAdvancementCalculator {
    override fun calculateAdvancement(gameState: GameState, eventType: Event.Type): Triple<BooleanArray, Int, Int> {
        // Create a copy of the current bases
        val newBases = BooleanArray(3) { i -> 
            when (i) {
                0 -> gameState.firstBase()
                1 -> gameState.secondBase()
                2 -> gameState.thirdBase()
                else -> false
            }
        }

        var runsScored = 0
        var outsAdded = 0

        when (eventType) {
            Event.Type.SINGLE -> {
                // Runners on 2nd and 3rd score
                if (newBases[2]) {
                    runsScored++
                }
                if (newBases[1]) {
                    runsScored++
                }

                // Runner on 1st advances to 2nd
                newBases[1] = newBases[0]

                // Batter reaches 1st
                newBases[0] = true

                // Clear 3rd and 2nd bases (runners scored)
                newBases[2] = false
            }
            Event.Type.DOUBLE -> {
                // Runners on 2nd and 3rd score
                if (newBases[2]) {
                    runsScored++
                }
                if (newBases[1]) {
                    runsScored++
                }

                // Runner on 1st advances to 3rd
                newBases[2] = newBases[0]

                // Clear 1st base
                newBases[0] = false

                // Batter reaches 2nd
                newBases[1] = true
            }
            Event.Type.TRIPLE -> {
                // All runners score
                if (newBases[2]) {
                    runsScored++
                }
                if (newBases[1]) {
                    runsScored++
                }
                if (newBases[0]) {
                    runsScored++
                }

                // Clear 1st and 2nd bases
                newBases[0] = false
                newBases[1] = false

                // Batter reaches 3rd
                newBases[2] = true
            }
            Event.Type.HOME_RUN -> {
                // All runners and batter score
                var runs = 1 // Batter scores

                // Count runners who score
                for (i in 0..2) {
                    if (newBases[i]) {
                        runs++
                        newBases[i] = false
                    }
                }

                runsScored = runs
            }
            Event.Type.WALK -> {
                // If bases loaded, runner on 3rd scores
                if (newBases[0] && newBases[1] && newBases[2]) {
                    runsScored++
                } else {
                    // Advance runners only if forced
                    if (newBases[0] && newBases[1]) {
                        newBases[2] = true
                    }
                    if (newBases[0]) {
                        newBases[1] = true
                    }
                }

                // Batter reaches 1st
                newBases[0] = true
            }
            Event.Type.GROUND_OUT -> {
                // Check for double play conditions: less than 2 outs and force out situation
                if (gameState.outs < 2) {
                    if (newBases[0]) { // Runner on first
                        if (newBases[1]) { // Runners on first and second
                            if (newBases[2]) { // Bases loaded
                                // Double play: runner at third out at home, runner at second out at third, others advance
                                runsScored = 0
                                newBases[2] = false // No runner at third (runner from second is out)
                                newBases[1] = true // Runner from first advances to second
                                newBases[0] = true // Batter is safe at first
                                outsAdded = 2
                            } else {
                                // Double play: runner at second out at third, runner at first out at second, batter safe at first
                                newBases[2] = false // No runner at third
                                newBases[1] = false // No runner at second
                                newBases[0] = true // Batter is safe at first
                                outsAdded = 2
                            }
                        } else {
                            // Double play: runner at first out at second, batter out at first
                            newBases[1] = false // No runner at second
                            newBases[0] = false // No runner at first (batter out)
                            outsAdded = 2
                        }
                    } else {
                        // No runner on first, batter is out at first
                        // Other runners don't advance
                        newBases[0] = false
                        outsAdded = 1
                    }
                } else {
                    // With 2 outs already, get the force out
                    outsAdded = 1

                    // Handle force outs based on base state
                    if (newBases[0]) { // Runner on first
                        if (newBases[1]) { // Runners on first and second
                            if (newBases[2]) { // Bases loaded
                                // Runner at third is out at home, other runners advance
                                runsScored = 0
                                newBases[2] = true // Runner from second advances to third
                                newBases[1] = true // Runner from first advances to second
                                newBases[0] = true // Batter is safe at first
                            } else {
                                // Runner at second is out at third, other runners advance
                                newBases[2] = false // Runner out at third
                                newBases[1] = true // Runner from first advances to second
                                newBases[0] = true // Batter is safe at first
                            }
                        } else {
                            // Runner at first is out at second, batter is safe at first
                            newBases[1] = false // No runner at second
                            newBases[0] = true // Batter is safe at first
                        }
                    } else {
                        // No runner on first, batter is out at first
                        // Other runners don't advance
                        newBases[0] = false
                    }
                }
            }
            Event.Type.LINE_OUT, Event.Type.POP_OUT, Event.Type.STRIKE_OUT -> {
                // Increment outs count
                outsAdded = 1
            }
            Event.Type.FLY_OUT -> {
                // Increment outs count
                outsAdded = 1

                // Sacrifice fly: if less than 2 outs and runner on third, the runner scores
                if (gameState.outs < 2 && newBases[2]) {
                    runsScored++
                    newBases[2] = false
                }
            }
            Event.Type.STOLEN_BASE -> {
                // Runner moves from first to second base
                newBases[1] = true
                newBases[0] = false
            }
            Event.Type.CAUGHT_STEALING -> {
                // Runner is removed from first base and an out is recorded
                newBases[0] = false
                outsAdded = 1
            }
        }

        return Triple(newBases, runsScored, outsAdded)
    }
}
