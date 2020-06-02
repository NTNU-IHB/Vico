package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.vico.VariableIdentifier
import no.ntnu.ihb.vico.chart.jaxb.TChartConfig
import no.ntnu.ihb.vico.chart.jaxb.TTimeSeriesChart
import no.ntnu.ihb.vico.chart.jaxb.TXYSeriesChart
import java.io.File
import javax.xml.bind.JAXB

object ChartLoader {

    fun load(configFile: File): List<AbstractDrawer> {
        return load(
            JAXB.unmarshal(
                configFile,
                TChartConfig::class.java
            )
        )
    }

    fun load(config: TChartConfig): List<AbstractDrawer> {
        return config.chart.map { chart ->
            when {
                chart.xyseries != null -> loadXYSeriesDrawer(chart.xyseries)
                chart.timeseries != null -> loadTimeSeriesDrawer(chart.timeseries)
                else -> throw AssertionError()
            }
        }
    }

    private fun loadTimeSeriesDrawer(chart: TTimeSeriesChart): TimeSeriesDrawer {
        return TimeSeriesDrawer.Builder(
            title = chart.title,
            label = chart.label
        ).apply {
            width(chart.width)
            height(chart.height)
            decimationFactor(chart.decimationFactor)
            isLive(chart.isLive)
            maxDuration(chart.maxDuration)
            chart.series.component.forEach { component ->
                registerSeries(component.name, component.variable.map { v -> v.name })
            }
        }.build()
    }

    private fun loadXYSeriesDrawer(chart: TXYSeriesChart): XYSeriesDrawer {
        return XYSeriesDrawer.Builder(
            title = chart.title,
            xLabel = chart.xLabel,
            yLabel = chart.yLabel
        ).apply {
            maxLength(chart.maxLength)
            width(chart.width)
            height(chart.height)
            decimationFactor(chart.decimationFactor)
            isLive(chart.isLive)
            maxLength(chart.maxLength)
            chart.series.forEach {
                registerSeries(
                    it.name,
                    VariableIdentifier(it.x.component, it.x.variable),
                    VariableIdentifier(it.y.component, it.y.variable)
                )
            }
        }.build()
    }

}
