package mm1.simulator

class Simulator {
    Double lambda
    Double mu
    Integer probes
    Integer seed
    Queue queue
    Simulator(Double lambda, Double mu, Integer probes, Integer seed) {
        this.lambda = lambda
        this.mu = mu
        this.probes = probes
        this.seed = seed
        this.queue = new Queue()
    }

    void simulate() {
        Poisson timeToDie = new Poisson(seed, lambda)
        0.upto(probes, {
            this.queue.addEvent(timeToDie.getRandomArrival(), EventType.ARRIVAL)
            this.queue.consumeEvent(new Poisson(seed, lambda).getRandomArrival())
        })
    }

}


class Queue {
    List<Event> eventList
    Double previousEventTime

    Queue(Double initTime) {
        eventList = new LinkedList<>()
        addEvent(initTime, EventType.ARRIVAL);
    }

    void addEvent(Double time, EventType type) {
        eventList.add(new Event(time, type))
    }

    Event consumeEvent(Double nextEventArrivalTime) {
        if(!eventList.isEmpty()) {
            Event event = eventList.first()
            process(event)
            previousEventTime = event.timeToStart
            eventList.remove(0)
            addEvent(nextEventArrivalTime, EventType.ARRIVAL)
            return event
        }
        else return null
    }

    void process(Event event) {

    }

    Boolean isEmpty() {
        return eventList.isEmpty()
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

class Result {
    Integer numberOfArrivals = 0;
    Integer numberOfDepartures = 0;
    Double averageNumberInSystem = 0;
    Double simulationTime = 0;
    Double averageTimeToService = 0;
    Double averageEventTime = 0;
    Double averageNumberOfWaitingTasks = 0;
}

enum EventType {
    ARRIVAL, DEPARTURE
}

