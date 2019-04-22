package mm1.model

// Fifo queue
class Queue extends EventConsumer {
    Queue(Double lambda, Integer seed, Integer initNumberOfEvents) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = lambda
        this.seed = seed
        this.previousEventTime = 0

        0.upto(initNumberOfEvents) {
            addEvent(generateEvent())
        }
    }

    @Override
    Event generateEvent() {
        def randomTime = Math.log(1.0 - generator.nextDouble()) / -poissonParameter
        return new Event(randomTime, EventType.QUEUE)
    }

    Boolean readyToArrive() {
        if(!this.eventList && !this.eventList.isEmpty())
            return this.clock > this.eventList.first().timeToStart
        else
            return true
    }
}
