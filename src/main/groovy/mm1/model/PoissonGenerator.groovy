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

    double generateRandomEventTimeInRange(double min, double max) {
        double uniformRandomInRange = min + (max - min) * generator.nextDouble()
        double poissonRandomWithMu = Math.log(1.0 - generator.nextDouble()) / -configuration.mu

        return uniformRandomInRange + poissonRandomWithMu
    }

    double generateRandomEventWithMean(double mean) {
        double L = Math.exp(-mean)
        int k = 0
        double p = 1.0
        p = p * this.generator.nextDouble()
        k++
        while (p > L) {
            p = p * this.generator.nextDouble()
            k++
        }
        return k - 1
    }
}