package mm1.simulator

class Poisson {
    Double randomArrival
    Poisson(double lambda) {
        randomArrival =  Math.log(1.0-Math.random())/-lambda
    }
}
