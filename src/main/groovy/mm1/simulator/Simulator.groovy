package mm1.simulator

import groovy.util.logging.Slf4j
import mm1.model.*

@Slf4j
class Simulator {
    Configuration configuration

    EventQueue queue
    System system

    // Statistics vars
    Integer eventsInSystem
    Integer eventsInQueue

    Double timeInSystem
    Double timeInQueue

    Double meanDelay


    Simulator(Configuration configuration) {
        this.configuration = configuration

        eventsInSystem = 0
        eventsInQueue = 0
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
        // Get system starting state
        this.system.updateSystemState()

        while (!eventList.isEmpty()) {
            def currentEvent = eventList.get()
            // We can't process message that just got in, system.process before adding events to queue
            this.system.process(currentEvent)

            if (currentEvent.type == EventType.MESSAGE) {
                this.queue.put(currentEvent)
                this.system.updateQueueStatistics()
            }
        }

        def times = system.processedEvents.collect { it.outTime - it.time}
        times = times.subList(configuration.warmUpTime, times.size())
        this.meanDelay = times.sum()/times.size()

        if (configuration.debug) {
            println("START =============== ")
            this.system.processedEvents.each { println(it.outTime - it.time)}
            println("STOP =============== " + this.meanDelay )
            println("P OFF =============== " + system.timeOff / (system.timeOff + system.timeOn) )
        }

        // Get system end state
        this.system.updateSystemState()
    }
}










