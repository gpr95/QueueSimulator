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

        Point previousEvent
        List<Double> arrivalTime = new ArrayList<>()
        List<Double> resultTime = new ArrayList<>()
        Double sum = 0.0
        for(int i = 10; i < this.system.queueEvents.size() - 1; i++) {
            Point currentEvent = this.system.queueEvents[i]
            previousEvent = this.system.queueEvents[i-1]

            if(currentEvent.y == previousEvent.y + 1) {
                // Event added to queue
                arrivalTime.add(currentEvent.x)
            } else if(currentEvent.y == previousEvent.y - 1) {
                if (arrivalTime.empty) {
                    continue
                }
                // Get current event processing time
                Double processingTime = 0.0
                Point nextSystemEvent = this.system.systemsEvents.find { it.x > currentEvent.x}
                if(nextSystemEvent != null) {
                    processingTime = nextSystemEvent.x - currentEvent.x
                    // Event removed from queue
                    double queueTime = currentEvent.x - arrivalTime.last()
                    resultTime.add(queueTime + processingTime)
                    //println(resultTime.last())
                    sum += queueTime + processingTime
                }
            }
//
        }
        this.meanDelay = sum/resultTime.size()
        // Get system end state
        this.system.updateSystemState()

        // log.info this.system.toString()
    }
}










