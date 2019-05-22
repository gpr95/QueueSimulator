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

            double mi = 1 / configuration.d
            double pOn = configuration.switching ? configuration.econ / ( configuration.econ + configuration.ecoff ) : 1
            double pOff = configuration.switching ? configuration.ecoff / ( configuration.econ + configuration.ecoff ) : 0
            double ecoff = configuration.ecoff
            // E[T] = (lambda/(mu*Pon) + E(Coff)*Poff) / ((1 - lambda/(mu*Pon))*lambda)
            double ro = lambda / (mi * pOn)
            double value = (ro + lambda * ecoff * pOff) / ((1 - ro) * lambda)
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
