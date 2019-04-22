package mm1.model

abstract class EventConsumer {
    List<Event> eventList
    Double previousEventTime
    Integer seed
    Double poissonParameter
    Random generator

    Double clock
    Random clockGenerator
    EventConsumer(Integer seed) {
        this.generator = new Random(seed)
        this.clockGenerator = new Random()
        clock = 0.0
    }

    void addEvent(Event event) {
        eventList.add(event)
        eventList.sort()
    }

    abstract Event generateEvent()

    Event consumeEvent() {
        if(!eventList.isEmpty()) {
            Event event = eventList.first()
            this.previousEventTime = event.timeToStart
            this.eventList.remove(0)

            return event
        }
        else return null
    }

    void tickOfTheClock() {
        this.clock += Math.log((Double) 1.0 - this.clockGenerator.nextDouble())/-this.poissonParameter
    }

    Boolean readyToConsume() {
        return this.clock > this.eventList.first().timeToStart
    }

    Boolean isEmpty() {
        return this.eventList.size() == 0
    }

    Boolean isConsuming() {
        return this.eventList.size() > 0
    }
}