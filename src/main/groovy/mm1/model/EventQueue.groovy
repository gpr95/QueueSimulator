package mm1.model

// Fifo queue
class EventQueue {
    Queue<Event> eventsInQueue

    EventQueue() {
        this.eventsInQueue = new LinkedList<>()
    }

    void put(Event event) {
        eventsInQueue << event
    }

    Event get() {
        return eventsInQueue.poll()
    }
}
