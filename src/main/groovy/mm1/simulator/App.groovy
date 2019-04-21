/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package mm1.simulator

class App {
    String getGreeting() {
        return 'MM1 simulator starting...'
    }

    Properties readProperties() {
        Properties properties = new Properties()
        File propertiesFile = new File(getClass().getResource('/user-input.properties').toURI())
        propertiesFile.withInputStream {
            properties.load(it)
        }

        properties.each { println "$it.key -> $it.value" }

        return properties
    }

    static void main(String[] args) {
        println new App().greeting
        Properties properties = new App().readProperties()
        def lambda = (Double) properties.lambda
        def mu = (Double) properties.mu
        def probes = properties.probes as Integer

        Simulator simulation = new Simulator(lambda, mu, probes)
        simulation.simulate()
    }
}
