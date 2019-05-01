package mm1.model

import groovy.transform.ToString


@ToString(includeNames = true)
class System extends PoissonGenerator{

    EventQueue eventQueue
    SystemState state = SystemState.SYSTEM_ON

    Event currentEvent = null
    double remainingProcessingTime = 0

    double lastEventTime = 0
    double timePassed = 0

    // Statistics
    double timeProcessing = 0
    double timeIdle = 0
    double timeOn = 0
    double timeOff = 0

    System(EventQueue queue, Configuration configuration) {
        super(configuration)
        this.eventQueue = queue
    }

    void process(Event currentEvent) {
        timePassed = currentEvent.time - lastEventTime

        if (state == SystemState.SYSTEM_OFF) {
            timeOff += timePassed
            finish(currentEvent)
            return
        }
        timeOn += timePassed

        while (timePassed > 0) {
            processEvent()
        }

        finish(currentEvent)
    }

    void processEvent() {

        if (currentEvent == null) {
            currentEvent = eventQueue.get()
            remainingProcessingTime = getEventProcessingTime()
        }

        // Queue is empty
        if (currentEvent == null) {
            timeIdle += timePassed
            timePassed = 0
            return
        }

        timePassed -= remainingProcessingTime

        if (timePassed > 0) {
            // Event processed
            currentEvent = null
            timeProcessing += remainingProcessingTime

        } else {
            // Event partially processed
            timeProcessing += (remainingProcessingTime + timePassed)
            remainingProcessingTime = -timePassed
            timePassed = 0
        }
    }

    void finish(Event currentEvent) {

        if (currentEvent.type == EventType.SERVER_ON)
            state = SystemState.SYSTEM_ON
        else if (currentEvent.type == EventType.SERVER_OFF)
            state = SystemState.SYSTEM_OFF

        lastEventTime = currentEvent.time
    }

    double getEventProcessingTime() {
        def modifier
        if (generator.nextBoolean())
            modifier = 1
        else
            modifier = -1
        return configuration.d + modifier * generateRandomEventTime() / 1000
    }
}

enum SystemState {
    SYSTEM_ON, SYSTEM_OFF
}
