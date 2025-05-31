data class Event(
    val type: Type,
    val bases: BooleanArray,
    val runsScored: Int,
    val outsAdded: Int
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

        if (runsScored != other.runsScored) return false
        if (outsAdded != other.outsAdded) return false
        if (type != other.type) return false
        if (!bases.contentEquals(other.bases)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = runsScored
        result = 31 * result + outsAdded
        result = 31 * result + type.hashCode()
        result = 31 * result + bases.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "Event(type=$type, bases=${bases.contentToString()}, runsScored=$runsScored, outsAdded=$outsAdded)"
    }
}
