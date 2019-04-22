package mm1.model

class Queue extends EventConsumer{
    Queue(Double lambda, Integer seed) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = lambda
        this.seed = seed
        this.previousEventTime = 0

        addEvent(generateEvent())
    }

    @Override
    Event consumeEvent() {
        if(!eventList.isEmpty()) {
            Event event = eventList.first()
            this.previousEventTime = event.timeToStart
            this.eventList.remove(0)
            addEvent(generateEvent())

            return event
        }
        else return null
    }

    @Override
    Event generateEvent() {
        def randomTime =  Math.log(1.0-generator.nextDouble())/-poissonParameter
        return new Event(randomTime, EventType.QUEUE)
    }
}
