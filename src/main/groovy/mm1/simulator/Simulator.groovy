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
        0.upto(probes, {
            Poisson timeToDie = new Poisson(seed, lambda)

            this.queue.addEvent(timeToDie.getRandomArrival(), EventType.ARRIVAL)
            this.queue.consumeEvent()
        })
    }

}


class Queue {
    List<Event> eventList

    Queue(Double initTime) {
        eventList = new LinkedList<>()
        addEvent(initTime, EventType.ARRIVAL);
    }

    void addEvent(Double time, EventType type) {
        eventList.add(new Event(time, type))
    }

    Event consumeEvent() {
        if(!eventList.isEmpty()) {
            Event event = eventList.first()
            eventList.remove(0)
            return event
        }
        else return null
    }

    Boolean isEmpty() {
        return eventList.isEmpty()
    }
}

class Event {
    Double time
    EventType type

    Event(Double time, EventType type) {
        this.time = time
        this.type = type
    }
}

enum EventType {
    ARRIVAL
}

