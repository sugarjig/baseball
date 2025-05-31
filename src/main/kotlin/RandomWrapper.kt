import kotlin.random.Random

class RandomWrapper(private val random: Random) : RandomGenerator {
    override fun nextDouble(): Double {
        return random.nextDouble()
    }
}