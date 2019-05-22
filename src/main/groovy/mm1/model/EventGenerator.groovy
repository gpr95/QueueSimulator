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
        def time = generateRandomEventWithMean(1/configuration.econ)
        def isOn = true
        while (time < configuration.simulationDuration) {

            Event e
            if (isOn) {

                e = new Event(time, EventType.SERVER_OFF)
                time += generateRandomEventWithMean(1/configuration.ecoff)
                isOn = false
            } else {

                e = new Event(time, EventType.SERVER_ON)
                time += generateRandomEventWithMean(1/configuration.econ)
                isOn = true
            }
            eventList.put(e)
        }
    }

    void generateMessages(EventList eventList) {
        def time = getMessageTimeGenerator()
        while (time < configuration.simulationDuration) {

            if (configuration.task == 3) {
                eventList.put(new Event(time, EventType.MESSAGE, generateRandomEventWithMean(1/generateRandomEventLinearWithOffset())))
            } else {
                eventList.put(new Event(time, EventType.MESSAGE, generateRandomEventWithMean(1/configuration.d)))
            }

            time += getMessageTimeGenerator()
        }
    }

    double getMessageTimeGenerator() {
        return generateRandomEventWithMean(configuration.lambda)
    }
}
