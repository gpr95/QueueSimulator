package mm1.model


class System extends EventConsumer {
    System(Double mu, Integer seed) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = mu
        this.seed = seed
        this.previousEventTime = 0
    }

    @Override
    Event generateEvent() {
        def randomTime = Math.log(1.0 - generator.nextDouble()) / -poissonParameter
        return new Event(randomTime, EventType.SYSTEM)
    }
}
