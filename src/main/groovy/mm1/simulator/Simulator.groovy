package mm1.simulator

import groovy.util.logging.Slf4j

@Slf4j
class Simulator {
    Double lambda
    Double mu
    Integer probes
    Integer seed
    Queue queue
    System system

    // Statistics vars
    Integer eventsInSystem
    Integer eventsInQueue

    Double timeInSystem
    Double timeInQueue



    Simulator(Double lambda, Double mu, Integer probes, Integer seed) {
        this.lambda = lambda
        this.mu = mu
        this.probes = probes
        this.seed = seed

        eventsInSystem = 0
        eventsInQueue = 3
        timeInSystem = 0
        timeInQueue = 0
    }


    void simulate() {
        // Single event is generated in queue constructor
        this.queue = new Queue(this.lambda, this.seed)
        this.system = new System(this.mu, this.seed)

        for(int i = 0; i < this.probes; i++) {
            log.debug "Real events in queue: " + this.queue.eventList.size() + "; Real events in system: " + this.system.eventList.size()
            log.debug "Events in queue: " + this.eventsInQueue + "; events in system: " + this.eventsInSystem

            // Get event from queue and put to the system
            if(system.isEmpty()) {
                log.debug "System is empty"
                if(queue.readyToConsume()) {
                    log.debug "Queue ready to consume"

                    // Single event is removed from queue, single event is added to queue
                    this.eventsInQueue++
                    Event eventInQueue = queue.consumeEvent()
                    // Queue event consumption add new event to the system
                    Event eventInSystem = consume(eventInQueue)
                    system.addEvent(eventInSystem)
                }
            }

            // Consume event in system
            if(system.isConsuming()) {
                log.debug "System is consuming"
                if(system.readyToConsume()) {
                    log.debug "System ready to consume"
                    consume(system.eventList.first())
                }
            }

            // Simulate time of queue and system
            queue.tickOfTheClock()
            system.tickOfTheClock()
        }
    }

    Event consume(Event event) {
        switch(event?.type) {
            case EventType.QUEUE:
                if(this.eventsInSystem == 0) {
                    this.queue.clock -= event.timeToStart
                    this.eventsInQueue--
                    this.eventsInSystem++
                    Event eventToSystem = this.system.generateEvent()
                    event = eventToSystem
                }
                break
            case EventType.SYSTEM:
                this.system.clock -= event.timeToStart
                this.eventsInSystem--
                event = this.system.consumeEvent()
                break
        }

        return event
    }
}



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
}


class Queue extends EventConsumer{
    Queue(Double lambda, Integer seed) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = lambda
        this.seed = seed
        this.previousEventTime = 0

        addEvent(generateEvent())
        addEvent(generateEvent())
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

class System extends EventConsumer{
    System(Double mu, Integer seed) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = mu
        this.seed = seed
        this.previousEventTime = 0
    }

    @Override
    Event generateEvent() {
        def randomTime =  Math.log(1.0-generator.nextDouble())/-poissonParameter
        return new Event(randomTime, EventType.SYSTEM)
    }

    Boolean isConsuming() {
        return this.eventList.size() > 0
    }

    Boolean isEmpty() {
        return this.eventList.size() == 0
    }


}

class Event implements Comparable<Event>{
    Double timeToStart
    EventType type

    Event(Double timeToStart, EventType type) {
        this.timeToStart = timeToStart
        this.type = type
    }

    @Override int compareTo(Event event) {
        if(this.timeToStart < event.timeToStart)
            return -1
        else if(this.timeToStart > event.timeToStart)
            return 1
        else return 0
    }
}

enum EventType {
    QUEUE, SYSTEM
}

