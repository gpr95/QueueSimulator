package mm1.simulator


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
            println("Events in queue: " + this.eventsInQueue + "; events in system: " + this.eventsInSystem)
            // Simulate time
            queue.tickOfTheClock()

            // If system is empty consume directly to the system
            if(this.eventsInSystem == 0) {
                // Single event is removed from queue, single event is added to queue
                Event eventInQueue = queue.consumeEvent()
                // Queue event consumption add new event to the system
                Event eventInSystem = consume(eventInQueue, system, queue)
                system.addEvent(eventInSystem)
            }
            // If system is busy just add event to queue
            else {
                // Simulate time
                system.tickOfTheClock()
                Event systemEvent = system.eventList.first()
                if (system.clock > systemEvent.timeToStart) {
                    println("consuming system event")
                    consume(systemEvent, system, queue)
                }

                // Single event is added to queue
                this.eventsInQueue++
                queue.addEvent(queue.generateEvent())
            }
        }
    }

    Event consume(Event event, System system, Queue queue) {
        if(event == null)
            return
        switch(event.type) {
            case EventType.QUEUE:
                if(this.eventsInSystem == 0) {
                    queue.clock -= event.timeToStart
                    this.eventsInQueue--
                    this.eventsInSystem++
                    Event eventToSystem = system.generateEvent()
                    event = eventToSystem
                }
                break
            case EventType.SYSTEM:
                system.clock -= event.timeToStart
                system.consumeEvent()
                this.eventsInSystem--
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

