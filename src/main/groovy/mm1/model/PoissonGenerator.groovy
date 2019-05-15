package mm1.model

abstract class PoissonGenerator {
    Random generator
    Configuration configuration

    PoissonGenerator() {}

    PoissonGenerator(Configuration configuration) {
        this.generator = new Random(configuration.seed)
        this.configuration = configuration
    }

    double generateRandomEventWithMean(double mean) {
        return Math.log(1 - generator.nextDouble())/(-mean)
    }

    double generateRandomNumber() {
        int sign = generator.nextBoolean() ? 1 : -1;
        return sign * Math.log(1 - generator.nextDouble())/(-1)/1000
    }
}