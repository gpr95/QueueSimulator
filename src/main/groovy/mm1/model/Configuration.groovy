package mm1.model

import groovy.transform.ToString

@ToString(includeNames = true)
class Configuration {
    double lambda
    double mu
    int probes
    int seed

    int numOfSimulations

    boolean switching

    double simulationDuration
    double econ
    double ecoff
    double d

    String outputDir
    String templateName
    String reportName

    Configuration(Properties properties) {
        this.lambda = properties.lambda as double
        this.mu = properties.mu as double
        this.probes = properties.probes as int
        this.seed = properties.seed as int
        this.numOfSimulations = properties.numOfSimulations as int
        this.switching = properties.switching as boolean
        this.simulationDuration = properties.simulationDuration as double
        this.econ = properties.econ as double
        this.ecoff = properties.ecoff as double
        this.d = properties.d as double
        this.outputDir = properties.outputDir as String
        this.templateName = properties.templateName as String
        this.reportName = properties.reportName as String
    }
}