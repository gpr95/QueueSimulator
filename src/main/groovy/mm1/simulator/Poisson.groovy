package mm1.simulator

class Poisson {
    Double randomArrival
    Poisson(Integer seed, double lambda) {
        Random generator = new Random(seed)
        randomArrival =  Math.log(1.0-generator.nextDouble())/-lambda
    }
}
