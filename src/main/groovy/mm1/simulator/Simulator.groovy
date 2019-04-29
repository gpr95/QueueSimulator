package mm1.simulator

import groovy.util.logging.Slf4j
import mm1.model.*

@Slf4j
class Simulator {
//    Double lambda
//    Double mu
//    Integer probes
//    Integer seed
    Configuration configuration

    EventQueue queue
    System system

    // Statistics vars
    Integer eventsInSystem
    Integer eventsInQueue

    Double timeInSystem
    Double timeInQueue


    Simulator(Configuration configuration) {
        this.configuration = configuration

        eventsInSystem = 0
        eventsInQueue = 10
        timeInSystem = 0
        timeInQueue = 0
    }

    /**
     * It simulates M/M/1 queue.
     * Events arrive with Poisson(lambda) distribution.
     * Events are consumed by server with Poisson(mu) distribution.
     */
    void simulate(EventList eventList) {
        // Single event is generated in queue constructor
        this.queue = new EventQueue()
        // System is empty by default
        this.system = new System(this.queue, configuration)

        while (!eventList.isEmpty()) {
            def currentEvent = eventList.get()

            if (currentEvent.type == EventType.MESSAGE)
                this.queue.put(currentEvent)

            this.system.process(currentEvent)
        }

        println(this.system)

//        for (int i = 0; i < this.probes; i++) {
//            log.debug "Real events in queue: " + this.queue.eventsInQueue.size() + "; Real events in system: " + this.system.eventsInQueue.size()
//            log.debug "Events in queue: " + this.eventsInQueue + "; events in system: " + this.eventsInSystem
//
//            // Events arrive with Poisson(lambda) independently
//            // to simulate queue delay: if clock > timeToStart
//            if (queue.readyToArrive()) {
//                log.debug "EventQueue ready to consume"
//                this.eventsInQueue++
//                queue.addEvent(queue.generateEvent())
//            }
//
//            // Time passes independently
//            // Simulate time of queue and system
//            queue.tickOfTheClock()
//            system.tickOfTheClock()
//
//            // Get event from queue and put to the system
//            if (system.isEmpty()) {
//                log.debug "System is empty"
//
//                // Single event is removed from queue
//                this.eventsInQueue--
//                Event eventFromQueue = queue.pop()
//                Event eventInSystem = consume(eventFromQueue)
//                // Add event to system
//                this.eventsInSystem++
//                system.addEvent(eventInSystem)
//            }
//
//            // Consume event in system if there is any event inside
//            if (system.isConsuming()) {
//                log.debug "System is consuming"
//                // if clock > timeToStart
//                if (system.readyToConsume()) {
//                    log.debug "System ready to consume"
//                    consume(system.eventsInQueue.first())
//                }
//            }
//        }
    }

    Event consume(Event event) {
        switch (event?.type) {
            case EventType.QUEUE:
                if (this.eventsInSystem == 0) {
                    // Scenario when event from queue need to be added to system
                    this.queue.clock -= event.timeToStart
                    Event eventToSystem = this.system.generateEvent()
                    event = eventToSystem
                }
                break
            case EventType.SYSTEM:
                // Scenario when event from system could be removed
                this.system.clock -= event.timeToStart
                this.eventsInSystem--
                event = this.system.pop()
                break
        }

        return event
    }
}










