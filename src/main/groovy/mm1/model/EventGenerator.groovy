package mm1.model

class EventGenerator extends PoissonGenerator {

    EventGenerator(Configuration configuration) {
        super(configuration)
    }

    void generate(EventList eventList) {

        if (configuration.switching)
            generateSwitchingEvents(eventList)
        generateMessages(eventList)

        eventList.put(new Event(configuration.simulationDuration, EventType.END))
    }

    void generateSwitchingEvents(EventList eventList) {
        def time = configuration.econ
        def isOn = true
        while (time < configuration.simulationDuration) {

            Event e
            if (isOn) {

                e = new Event(time, EventType.SERVER_OFF)
                time += configuration.ecoff
                isOn = false
            } else {

                e = new Event(time, EventType.SERVER_ON)
                time += configuration.econ
                isOn = true
            }
            eventList.put(e)
        }
    }

    void generateMessages(EventList eventList) {
        def time = generateRandomEventTime()
        while (time < configuration.simulationDuration) {

            eventList.put(new Event(time, EventType.MESSAGE))
            time += generateRandomEventTime()
        }
    }
}
