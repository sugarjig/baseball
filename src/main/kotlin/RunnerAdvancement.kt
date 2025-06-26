data class RunnerAdvancement(
    val startBase: Base,
    val endBase: Base,
    val isOut: Boolean
) {
    enum class Base {
        BATTER, FIRST, SECOND, THIRD, HOME
    }
}
