data class Event(
    val type: Type,
    val runnerAdvancements: List<RunnerAdvancement>,
) {
    enum class Type(val description: String) {
        SINGLE("Single"),
        DOUBLE("Double"),
        TRIPLE("Triple"),
        HOME_RUN("Home Run"),
        WALK("Walk"),
        GROUND_OUT("Ground Out"),
        FLY_OUT("Fly Out"),
        LINE_OUT("Line Out"),
        POP_OUT("Pop Out"),
        STRIKE_OUT("Strike Out"),
        STOLEN_BASE("Stolen Base"),
        CAUGHT_STEALING("Caught Stealing");

        override fun toString(): String {
            return description
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (type != other.type) return false
        if (runnerAdvancements != other.runnerAdvancements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + runnerAdvancements.hashCode()
        return result
    }

    override fun toString(): String {
        return "Event(type=$type, runnerAdvancements=$runnerAdvancements)"
    }
}
