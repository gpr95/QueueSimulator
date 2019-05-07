package mm1

import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import mm1.model.Configuration
import mm1.model.EventGenerator
import mm1.model.EventList
import mm1.model.EventType
import mm1.simulator.Simulator
import mm1.simulator.Statistics

@Slf4j
class App {
    static String getGreeting() {
        return 'MM1 simulator starting...'
    }

    Properties readProperties(String fileName) {
        Properties properties = new Properties()
        getClass().getResource(fileName).withInputStream {
            properties.load(it)
        }

        return properties
    }

    static void main(String[] args) {
        log.info greeting

        // Choose task to do
        int taskID = 1
        Properties properties = new App().readProperties("/task${taskID}.properties")
        Configuration config = new Configuration(properties)


        // Debug option
        Boolean debug = false
        if(debug) {
            config.numOfSimulations = 1
        }

        Statistics statistics = new Statistics()

        // Run multiple lambda values in range <lowerValueOfArrivals, upperValueOfArrivals>
        // from_value.step to_value step_value {}
        for(Double lambda = config.lowerValueOfArrivals; lambda < config.upperValueOfArrivals;
            lambda += (config.upperValueOfArrivals - config.lowerValueOfArrivals)/100  ) {
            Double meanDelaySystemTimeSum = 0.0
            // Run multiple simulations
            for(int i = 0; i < config.numOfSimulations; i++) {
                EventList eventList = getEventList(config, lambda)

                log.info(eventList.eventList.size().toString() + "/" +
                        eventList.eventList.findAll { it.type == EventType.MESSAGE }.size().toString())


                Simulator simulation = new Simulator(config)
                simulation.simulate(eventList)
                meanDelaySystemTimeSum += simulation.system.timeProcessing


                // generateHTMLReport(config, simulation.system, i)

                // change seed
                config.seed++
            }

            statistics.addStatistics(meanDelaySystemTimeSum/config.numOfSimulations, config.lambda)

        }

        statistics.plot()

        /*
        Task1:
            Operate mean arrival time (lambda) between lowerValueOfArrivals and upperValueOfArrivals
            and plot mean delay of the system E[T]

        Task2:
            Turn on and turn off the system with probability of Poff and Pon, operate  mean arrival time (lambda)
            between lowerValueOfArrivals and upperValueOfArrivals and plot mean delay of the system E[T].
            Plot also theoretical values of E[T] as
            E[T] = (lambda/(mu*Pon) + E(Coff)*Poff) / ((1 - lambda/(mu*Pon))*lambda)
        Task3:
            The same as Task2 but system event handling has uniform distribution between (0.1;0.15).
        */
        //TODO: add utility to generate system mu as uniform distribution
        //TODO: add statistics gathering during simulations, add them to plot E(T)(lambda)
    }

    static void generateHTMLReport(Configuration configuration, mm1.model.System system, int simulationNumber) {
        def templateText = getClass().getResource(configuration.templateName).getText()
        def engine = new SimpleTemplateEngine().createTemplate(templateText)
        def template = engine.make(["title": "Report $simulationNumber", "data": system])
        File file = new File(configuration.outputDir + File.separator + sprintf(configuration.reportName, simulationNumber))
        file.getParentFile().mkdirs()
        file.write(template.toString())
    }

    static EventList getEventList(Configuration config, Double lambda) {
        config.lambda = lambda
        EventGenerator generator = new EventGenerator(config)
        EventList eventList = new EventList()
        generator.generate(eventList)

        return eventList
    }
}

