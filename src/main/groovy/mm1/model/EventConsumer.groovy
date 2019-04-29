package mm1.model

abstract class EventConsumer {
//    List<Event> eventsInQueue
//    Double previousEventTime
//    Integer seed
//    Double poissonParameter
    Random generator
    Configuration configuration
//    Double clock
//    Random clockGenerator
    EventConsumer(Configuration configuration) {
        this.generator = new Random(configuration.seed)
        this.configuration = configuration
//        this.clockGenerator = new Random()
//        clock = 0.0
    }

    double generateRandomEventTime() {
        return Math.log(1.0 - generator.nextDouble()) / -configuration.mu
    }

//    void addEvent(Event event) {
//        eventsInQueue.add(event)
//        eventsInQueue.sort()
//    }
//
//    abstract Event generateEvent()
//
//    Event pop() {
//        if(!eventsInQueue.isEmpty()) {
//            Event event = eventsInQueue.first()
//            this.previousEventTime = event.timeToStart
//            this.eventsInQueue.remove(0)
//            return event
//        }
//        else return null
//    }
//
//    void tickOfTheClock() {
//        Double randomPoissonValue = Math.log((Double) 1.0 - this.clockGenerator.nextDouble())/-this.poissonParameter
//        this.clock += randomPoissonValue/4
//    }
//
//    Boolean readyToConsume() {
//        if(!this.eventsInQueue && !this.eventsInQueue.isEmpty())
//            return this.clock > this.eventsInQueue.first().timeToStart
//        else
//            return true
//    }
//
//    Boolean isEmpty() {
//        return this.eventsInQueue.size() == 0
//    }
//
//    Boolean isConsuming() {
//        return this.eventsInQueue.size() > 0
//    }
}