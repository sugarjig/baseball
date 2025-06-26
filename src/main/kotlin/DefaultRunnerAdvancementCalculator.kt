class DefaultRunnerAdvancementCalculator : RunnerAdvancementCalculator {
    override fun calculateAdvancements(gameState: GameState, eventType: Event.Type): List<RunnerAdvancement> {
        val runnerAdvancements = mutableListOf<RunnerAdvancement>()

        when (eventType) {
            Event.Type.SINGLE -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.FIRST,
                        false,
                    )
                )

                if (gameState.firstBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.FIRST,
                            RunnerAdvancement.Base.SECOND,
                            false,
                        )
                    )
                }

                if (gameState.secondBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.SECOND,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }

                // Runners on 2nd and 3rd score
                if (gameState.thirdBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.THIRD,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }
            }
            Event.Type.DOUBLE -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.SECOND,
                        false,
                    )
                )

                if (gameState.firstBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.FIRST,
                            RunnerAdvancement.Base.THIRD,
                            false,
                        )
                    )
                }

                if (gameState.secondBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.SECOND,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }

                if (gameState.thirdBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.THIRD,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }
            }
            Event.Type.TRIPLE -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.THIRD,
                        false,
                    )
                )

                if (gameState.firstBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.FIRST,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }

                if (gameState.secondBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.SECOND,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }

                if (gameState.thirdBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.THIRD,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }
            }
            Event.Type.HOME_RUN -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.HOME,
                        false,
                    )
                )

                if (gameState.firstBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.FIRST,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }

                if (gameState.secondBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.SECOND,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }

                if (gameState.thirdBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.THIRD,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }
            }
            Event.Type.WALK -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.FIRST,
                        false,
                    )
                )

                if (gameState.firstBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.FIRST,
                            RunnerAdvancement.Base.SECOND,
                            false,
                        )
                    )

                    if (gameState.secondBase()) {
                        runnerAdvancements.add(
                            RunnerAdvancement(
                                RunnerAdvancement.Base.SECOND,
                                RunnerAdvancement.Base.THIRD,
                                false,
                            )
                        )

                        if (gameState.thirdBase()) {
                            runnerAdvancements.add(
                                RunnerAdvancement(
                                    RunnerAdvancement.Base.THIRD,
                                    RunnerAdvancement.Base.HOME,
                                    false,
                                )
                            )
                        }
                    }
                }
            }
            Event.Type.GROUND_OUT -> {
                // Check for double play conditions: less than 2 outs and force out situation
                if (gameState.outs < 2) {
                    if (gameState.firstBase()) { // Runner on first
                        if (gameState.secondBase()) { // Runners on first and second
                            if (gameState.thirdBase()) { // Bases loaded
                                // Double play: runner at third out at home, runner at second out at third, others advance
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.BATTER,
                                        RunnerAdvancement.Base.FIRST,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.FIRST,
                                        RunnerAdvancement.Base.SECOND,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.SECOND,
                                        RunnerAdvancement.Base.THIRD,
                                        true,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.THIRD,
                                        RunnerAdvancement.Base.HOME,
                                        true,
                                    )
                                )
                            } else {
                                // Double play: runner at second out at third, runner at first out at second, batter safe at first
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.BATTER,
                                        RunnerAdvancement.Base.FIRST,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.FIRST,
                                        RunnerAdvancement.Base.SECOND,
                                        true,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.SECOND,
                                        RunnerAdvancement.Base.THIRD,
                                        true,
                                    )
                                )
                            }
                        } else {
                            // Double play: runner at first out at second, batter out at first
                            runnerAdvancements.add(
                                RunnerAdvancement(
                                    RunnerAdvancement.Base.BATTER,
                                    RunnerAdvancement.Base.FIRST,
                                    true,
                                )
                            )
                            runnerAdvancements.add(
                                RunnerAdvancement(
                                    RunnerAdvancement.Base.FIRST,
                                    RunnerAdvancement.Base.SECOND,
                                    true,
                                )
                            )
                        }
                    } else {
                        // No runner on first, batter is out at first
                        // Other runners don't advance
                        runnerAdvancements.add(
                            RunnerAdvancement(
                                RunnerAdvancement.Base.BATTER,
                                RunnerAdvancement.Base.FIRST,
                                true,
                            )
                        )
                    }
                } else {
                    // With 2 outs already, get the force out
                    // Handle force outs based on base state
                    if (gameState.firstBase()) { // Runner on first
                        if (gameState.secondBase()) { // Runners on first and second
                            if (gameState.thirdBase()) { // Bases loaded
                                // Runner at third is out at home, other runners advance
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.BATTER,
                                        RunnerAdvancement.Base.FIRST,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.FIRST,
                                        RunnerAdvancement.Base.SECOND,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.SECOND,
                                        RunnerAdvancement.Base.THIRD,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.THIRD,
                                        RunnerAdvancement.Base.HOME,
                                        true,
                                    )
                                )
                            } else {
                                // Runner at second is out at third, other runners advance
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.BATTER,
                                        RunnerAdvancement.Base.FIRST,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.FIRST,
                                        RunnerAdvancement.Base.SECOND,
                                        false,
                                    )
                                )
                                runnerAdvancements.add(
                                    RunnerAdvancement(
                                        RunnerAdvancement.Base.SECOND,
                                        RunnerAdvancement.Base.THIRD,
                                        true,
                                    )
                                )
                            }
                        } else {
                            // Runner at first is out at second, batter is safe at first
                            runnerAdvancements.add(
                                RunnerAdvancement(
                                    RunnerAdvancement.Base.BATTER,
                                    RunnerAdvancement.Base.FIRST,
                                    false,
                                )
                            )
                            runnerAdvancements.add(
                                RunnerAdvancement(
                                    RunnerAdvancement.Base.FIRST,
                                    RunnerAdvancement.Base.SECOND,
                                    true,
                                )
                            )
                        }
                    } else {
                        // No runner on first, batter is out at first
                        // Other runners don't advance
                        runnerAdvancements.add(
                            RunnerAdvancement(
                                RunnerAdvancement.Base.BATTER,
                                RunnerAdvancement.Base.FIRST,
                                true,
                            )
                        )
                    }
                }
            }
            Event.Type.LINE_OUT, Event.Type.POP_OUT, Event.Type.STRIKE_OUT -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.BATTER,
                        true,
                    )
                )
            }
            Event.Type.FLY_OUT -> {
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.BATTER,
                        RunnerAdvancement.Base.BATTER,
                        true,
                    )
                )

                // Sacrifice fly: if less than 2 outs and runner on third, the runner scores
                if (gameState.outs < 2 && gameState.thirdBase()) {
                    runnerAdvancements.add(
                        RunnerAdvancement(
                            RunnerAdvancement.Base.THIRD,
                            RunnerAdvancement.Base.HOME,
                            false,
                        )
                    )
                }
            }
            Event.Type.STOLEN_BASE -> {
                // Runner moves from first to second base
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.FIRST,
                        RunnerAdvancement.Base.SECOND,
                        false,
                    )
                )
            }
            Event.Type.CAUGHT_STEALING -> {
                // Runner is removed from first base and an out is recorded
                runnerAdvancements.add(
                    RunnerAdvancement(
                        RunnerAdvancement.Base.FIRST,
                        RunnerAdvancement.Base.SECOND,
                        true,
                    )
                )
            }
        }

        return runnerAdvancements
    }
}
