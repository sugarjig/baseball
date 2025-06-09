import org.apache.commons.rng.simple.RandomSource

fun main() {
    val runnerAdvancementCalculator = DefaultRunnerAdvancementCalculator()
    val seed = RandomSource.createLong()
    val rng = RandomSource.XO_RO_SHI_RO_128_PP.create(seed)
    val eventTypeSampler = CommonsRngSampler(rng)
    val eventGenerator = RandomEventGenerator(eventTypeSampler, runnerAdvancementCalculator)
    val observer = GameSimulatorPrinter()
    val gameSimulator = GameSimulator(eventGenerator, observer)
    gameSimulator.simulateGame()
}
