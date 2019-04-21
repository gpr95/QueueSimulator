package mm1.simulator

class Simulator {
    Double lambda
    Double mu
    Integer probes
    def queue
    Simulator(Double lambda, Double mu, Integer probes) {
        this.lambda = lambda
        this.mu = mu
        this.probes = probes
        this.queue = []
    }

    void addToQueue(Double timeToDie) {
        queue << timeToDie
    }

    void simulate() {
        0.upto(probes, {
            Poisson timeToDie = new Poisson(lambda)

            if(!queue.isEmpty()) {
                this.addToQueue(timeToDie)
            }
        })
    }

}
