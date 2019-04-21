package mm1.simulator

class Simulator {
    Double lambda
    Double mu
    Integer probes
    Integer seed
    Integer eventsInSystem
    Integer eventsInQueue

    Double timeInQueue
    Double timeInSystem


    Queue queue
    System system

    Simulator(Double lambda, Double mu, Integer probes, Integer seed) {
        this.lambda = lambda
        this.mu = mu
        this.probes = probes
        this.seed = seed
        this.system = new System(mu, seed)
        this.queue = new Queue(lambda, seed)
        eventsInSystem = 0
        eventsInQueue = 1
        timeInSystem = 0
        timeInQueue = 0
    }

    void simulate() {
        // Single event is generated in queue constructor
        Queue queue = new Queue(lambda, seed)
        System system = new System(mu, seed)

        0.upto(probes, {
            // Single event is removed from queue in consumer,
            Event event = queue.consumeEvent()

            //

            println(event.type)
            println(event.timeToStart)
            consume(event, system, queue)

            //

            Event systemEvent = system.consumeEvent()

            //

            println(event.type)
            println(event.timeToStart)
            consume(event, system, queue)

            //

            queue.addEvent(queue.generateEvent())
        })
    }

    void consume(Event event, System system, Queue queue) {
        if(!event?.timeToStart)
            return
        system.decreaseTimeRemainingToRemoveFromList(Math.abs(event.timeToStart - system.previousEventTime))
        queue.decreaseTimeRemainingToRemoveFromList(Math.abs(event.timeToStart - queue.previousEventTime))

        switch(event.type) {
            case EventType.QUEUE:
                this.eventsInQueue--
                this.eventsInSystem++

                Event eventToSystem = system.generateEvent()
                this.timeInSystem += eventToSystem.timeToStart
                system.addEvent(eventToSystem)
                break
            case EventType.SYSTEM:
                this.eventsInSystem--
                break
        }
    }

}



class EventConsumer {
    List<Event> eventList
    Double previousEventTime
    Integer seed
    Double poissonParameter
    Double timeRemainingToRemoveFromList
    Random generator

    EventConsumer(Integer seed) {
        generator = new Random(seed)
    }

    void addEvent(Event event) {
        boolean success = false

        for(Event oldEvent : eventList) {
            if(oldEvent > event) {
                eventList.add(eventList.indexOf(oldEvent), event)
                success = true
                break
            }
        }

        // Add to the end if time to start is the longest
        if(!success)
            eventList.add(event)
    }

    Event generateEvent() {
        def randomTime =  Math.log(1.0-generator.nextDouble())/-poissonParameter
        return new Event(randomTime, EventType.QUEUE)
    }

    Event consumeEvent() {
        if(!eventList.isEmpty()) {
            Event event = eventList.first()
            previousEventTime = event.timeToStart
            eventList.remove(0)
            return event
        }
        else return null
    }

    Boolean isEmpty() {
        return eventList.isEmpty()
    }

    void decreaseTimeRemainingToRemoveFromList(Double time) {
        this.timeRemainingToRemoveFromList -= time
        if(this.timeRemainingToRemoveFromList < 0)
            this.timeRemainingToRemoveFromList = 0
    }
}


class Queue extends EventConsumer{
    Queue(Double lambda, Integer seed) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = lambda
        this.seed = seed
        this.previousEventTime = 0
        this.timeRemainingToRemoveFromList = 0

        addEvent(generateEvent())
    }
}

class System extends EventConsumer{
    System(Double mu, Integer seed) {
        super(seed)
        this.eventList = new LinkedList<>()
        this.poissonParameter = mu
        this.seed = seed
        this.previousEventTime = 0
        this.timeRemainingToRemoveFromList = 0
    }
}

class Event implements Comparable<Event>{
    Double timeToStart
    Double duration
    EventType type

    Event(Double timeToStart, EventType type) {
        this.timeToStart = timeToStart
        this.type = type
    }

    Event(Double timeToStart, EventType type, Double duration) {
        this(timeToStart, type)
        this.duration = duration
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

