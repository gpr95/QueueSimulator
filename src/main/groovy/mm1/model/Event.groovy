package mm1.model

import groovy.transform.ToString

@ToString
class Event implements Comparable<Event>{
    Double time
    EventType type

    Event(Double timeToStart, EventType type) {
        this.time = timeToStart
        this.type = type
    }

    @Override int compareTo(Event event) {
        if(this.time < event.time)
            return -1
        else if(this.time > event.time)
            return 1
        else return 0
    }
}

enum EventType {
    MESSAGE, SERVER_ON, SERVER_OFF, END
}
