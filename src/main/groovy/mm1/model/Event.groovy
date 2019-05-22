package mm1.model

import groovy.transform.ToString

@ToString
class Event implements Comparable<Event>{
    Double time
    EventType type
    Double outTime
    Double processingTime

    Event(Double time, EventType type, processingTime=0) {
        this.time = time
        this.type = type
        this.processingTime = processingTime
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
