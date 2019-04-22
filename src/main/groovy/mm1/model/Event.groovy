package mm1.model

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
