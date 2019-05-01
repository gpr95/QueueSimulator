package mm1.model

class EventList {

    LinkedList<Event> eventList = new LinkedList<Event>()

    def put(Event event) {
        def success = false
        for(Event e : eventList) {
            if(e > event) {
                eventList.add(eventList.indexOf(e), event)
                success = true
                break
            }
        }
        if (!success)
            eventList << event
    }

    def get() {
        return eventList.poll()
    }

    def isEmpty() {
        return eventList.isEmpty()
    }


    @Override
    String toString() {
        return "EventList{" +
                "eventList=\n" + eventList.join('\n') +
                '}'
    }
}
