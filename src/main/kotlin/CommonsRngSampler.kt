import org.apache.commons.rng.UniformRandomProvider
import org.apache.commons.rng.sampling.DiscreteProbabilityCollectionSampler

class CommonsRngSampler(val rng: UniformRandomProvider) : EventSampler {
    override fun nextDouble(): Double {
        return rng.nextDouble()
    }

    override fun sample(collection: Map<Event.Type, Double>): Event.Type {
        return DiscreteProbabilityCollectionSampler(rng, collection).sample()
    }
}
