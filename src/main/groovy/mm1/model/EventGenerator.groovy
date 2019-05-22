package mm1.model

class EventGenerator extends PoissonGenerator {

    EventGenerator(Configuration configuration) {
        super(configuration)
    }

    void generate(EventList eventList) {

        generateMessages(eventList)
        if (configuration.switching)
            generateSwitchingEvents(eventList)


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
        if (configuration.numberOfMessages) {
            def numberOfMessages = configuration.numberOfMessages
            while (numberOfMessages--) {

                if (configuration.task == 3) {
                    eventList.putFast(new Event(time, EventType.MESSAGE, generateRandomEventLinearWithOffset()))
                } else {
                    eventList.putFast(new Event(time, EventType.MESSAGE, generateRandomEventWithMean(1/configuration.d)))
                }

                time += getMessageTimeGenerator()
            }
            return
        }
        while (time < configuration.simulationDuration) {

            if (configuration.task == 3) {
                eventList.put(new Event(time, EventType.MESSAGE, generateRandomEventLinearWithOffset()))
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
