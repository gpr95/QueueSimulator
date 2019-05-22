package mm1.model

import groovy.transform.ToString


@ToString(includeNames = true, ignoreNulls = true, excludes = "eventQueue, state, systemsEvents, queueEvents, systemState")
class System extends PoissonGenerator{

    EventQueue eventQueue
    SystemState state = SystemState.SYSTEM_ON

    Event systemEvent = null
    double remainingProcessingTime = 0

    double currentSystemTime = 0
    double currentTime = 0

    // Statistics
    double timeProcessing = 0
    double timeIdle = 0
    double timeOn = 0
    double timeOff = 0

    List<Point> systemsEvents = new ArrayList<>()
    List<Point> queueEvents = new ArrayList<>()
    List<Point> systemState = new ArrayList<>()

    LinkedList<Event> processedEvents = new LinkedList<Event>()

    System(EventQueue queue, Configuration configuration) {
        super(configuration)
        this.eventQueue = queue
    }

    void process(Event currentEvent) {

        currentTime = currentEvent.time

        doProcessing()

        currentSystemTime = currentTime

        switch (currentEvent.type) {
            case EventType.SERVER_ON:
                state = SystemState.SYSTEM_ON
                updateSystemState()
                break
            case EventType.SERVER_OFF:
                state = SystemState.SYSTEM_OFF
                updateSystemState()
                break
            case EventType.MESSAGE:
                break
        }
    }

    void doProcessing() {

        def timePassed = currentTime - currentSystemTime

        if (state == SystemState.SYSTEM_OFF) {
            timeOff += timePassed
            return
        } else {
            timeOn += timePassed
        }

        while (currentSystemTime < currentTime) {
            processEvent()
        }
    }

    void processEvent() {

        if (systemEvent == null) {
            systemEvent = eventQueue.get()
            if (systemEvent != null)
                remainingProcessingTime = systemEvent.processingTime

            updateSystemEvents()
            updateQueueStatistics()
        }

        // If after getting event from queue event still null -> queue is empty and nothing will come in this time-frame
        if (systemEvent == null) {

            timeIdle += currentTime - currentSystemTime

            currentSystemTime = currentTime
            updateSystemEvents()
            return
        }

        currentSystemTime += remainingProcessingTime

        if (currentSystemTime < currentTime) {
            // Event processed
            systemEvent.outTime = currentSystemTime
            processedEvents.add(systemEvent)
            systemEvent = null
            timeProcessing += remainingProcessingTime
        } else {
            // Event partially processed
            timeProcessing += (remainingProcessingTime - (currentSystemTime - currentTime))
            remainingProcessingTime = currentSystemTime - currentTime
            currentSystemTime = currentTime
        }
    }

    void updateQueueStatistics() {
        queueEvents.add(new Point(x: currentSystemTime, y: this.eventQueue.size()))
    }

    void updateSystemState() {
        systemState.add(new Point(x: currentSystemTime, y: -this.state.ordinal()+1))
    }

    void updateSystemEvents() {
        systemsEvents.add(new Point(x: currentSystemTime, y: this.systemEvent == null ? 0 : 1))
    }
}

enum SystemState {
    SYSTEM_ON, SYSTEM_OFF
}
