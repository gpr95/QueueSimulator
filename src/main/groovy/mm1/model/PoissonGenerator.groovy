package mm1.model

abstract class PoissonGenerator {
    Random generator
    Configuration configuration

    PoissonGenerator(Configuration configuration) {
        this.generator = new Random(configuration.seed)
        this.configuration = configuration
    }

    double generateRandomEventTime() {
        return Math.log(1.0 - generator.nextDouble()) / -configuration.mu
    }
}