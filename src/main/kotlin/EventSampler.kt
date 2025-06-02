interface EventSampler {
    fun nextDouble(): Double
    fun sample(collection: Map<Event.Type, Double>): Event.Type
}
