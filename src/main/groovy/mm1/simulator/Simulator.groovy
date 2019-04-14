package mm1.simulator

class Simulator {
    Double lambda
    Double mu
    Integer probes
    def queue
    Simulator(Properties properties) {
        lambda = (Double) properties.lambda
        mu = (Double) properties.mu
        probes = (Integer) properties.probes
        queue = []
    }


    void addToQueue(Double timeToDie) {
        queue << timeToDie
    }
    void simulate() {
        0.upto(probes, {
            Poisson timeToDie = new Poisson(lambda)

            if(!queue.isEmpty()) {
                queue << timeToDie
            }
        })
    }

}
