package mm1.simulator

import mm1.model.Configuration
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.ui.ApplicationFrame
import org.jfree.ui.RefineryUtilities

import java.awt.*
import java.util.List

class Statistics extends ApplicationFrame{
    List<Double> meanDelayInSystemList = new ArrayList<>()
    List<Double> lambdaList = new ArrayList<>()

    List<Simulator> simulationsList = new ArrayList<>()

    Configuration configuration

    Statistics(Configuration configuration){
        super("OAST")
        this.configuration = configuration
    }

    void addStatistics(Double meanDelayInSystem, Double lambda) {
        meanDelayInSystemList.add(meanDelayInSystem)
        lambdaList.add(lambda)
    }

    void addSimulation(Simulator simulator) {
        simulationsList.add(simulator)
    }

    void plot(){
        final XYSeries series = new XYSeries("Simulation")
        for(int i = 0; i < meanDelayInSystemList.size(); i++) {
            series.add(lambdaList.get(i), meanDelayInSystemList.get(i))
        }

        final XYSeries series2 = new XYSeries("Theory")
        for(int i = 0; i < meanDelayInSystemList.size(); i++) {
            double lambda = lambdaList.get(i)
            double mu = 0.125
            if(configuration.lowerValueOfService && configuration.upperValueOfService) {
                mu = configuration.lowerValueOfService +
                        (configuration.upperValueOfService - configuration.lowerValueOfService) * new Random().nextDouble()
            }
            double pOn = 0.5
            double pOff = 0.5
            double ecoff = configuration.ecoff
            // E[T] = (lambda/(mu*Pon) + E(Coff)*Poff) / ((1 - lambda/(mu*Pon))*lambda)
            double value = (lambda/(mu* pOn) + ecoff * pOff) / ((1 - lambda/(mu*pOn))*lambda)
            if (value < 1000)
                series2.add(lambda, value)
        }

        final XYSeriesCollection data = new XYSeriesCollection(series)
        data.addSeries(series2)
        println(series.items)
        println(series2.items)
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Mean delay time in System",
                "Lambda",
                "Mean Delay",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        )

        final ChartPanel chartPanel = new ChartPanel(chart)
        chartPanel.setPreferredSize(new Dimension(1000, 540))
        setContentPane(chartPanel)

        this.pack()
        RefineryUtilities.centerFrameOnScreen(this)
        this.setVisible(true)
    }

    void plotDemo(){
        final XYSeries series = new XYSeries("Simulation")
        series.add(1.0, 500.2)
        series.add(5.0, 694.1)
        series.add(4.0, 100.0)
        series.add(12.5, 734.4)
        series.add(17.3, 453.2)
        series.add(21.2, 500.2)
        series.add(21.9, null)
        series.add(25.6, 734.4)
        series.add(30.0, 453.2)
        final XYSeriesCollection data = new XYSeriesCollection(series)
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Mean delay time in System",
                "Lambda",
                "Mean Delay",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        )

        final ChartPanel chartPanel = new ChartPanel(chart)
        chartPanel.setPreferredSize(new Dimension(1000, 540))
        setContentPane(chartPanel)

        this.pack()
        RefineryUtilities.centerFrameOnScreen(this)
        this.setVisible(true)
    }
}
