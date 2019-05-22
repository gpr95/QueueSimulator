package mm1.model

import groovy.transform.ToString
import groovy.util.logging.Slf4j

@ToString(includeNames = true)
@Slf4j
class Configuration {
    double lowerValueOfArrivals
    double lambda
    double upperValueOfArrivals

    int seed
    int numOfSimulations

    boolean switching
    boolean debug

    double simulationDuration
    double econ
    double ecoff
    double d

    // Task 3 variables (only uniform distribution of this range in System
    double lowerValueOfService
    double upperValueOfService

    String outputDir
    String templateName
    String reportName

    int warmUpTime
    int task


    Configuration(Properties properties) {
        this.lowerValueOfArrivals = properties.lowerValueOfArrivals as double
        this.lambda = properties.lambda as double
        this.upperValueOfArrivals = properties.upperValueOfArrivals as double
        this.seed = properties.seed as int
        this.numOfSimulations = properties.numOfSimulations as int
        this.switching = Boolean.parseBoolean(properties.switching as String)
        this.debug = Boolean.parseBoolean(properties.debug as String)
        this.simulationDuration = properties.simulationDuration as double
        this.econ = properties.econ as double
        this.ecoff = properties.ecoff as double
        this.d = properties.d as double
        this.warmUpTime = properties.warmupTime as int
        this.task = properties.task as int
        this.outputDir = properties.outputDir as String
        this.templateName = properties.templateName as String
        this.reportName = properties.reportName as String

        if(properties.lowerValueOfService)
            this.lowerValueOfService = properties.lowerValueOfService as double
        if(properties.upperValueOfService)
            this.upperValueOfService = properties.upperValueOfService as double

        properties.each { log.info String.format("%-10s -> %-10s", it.key, it.value) }
    }
}
