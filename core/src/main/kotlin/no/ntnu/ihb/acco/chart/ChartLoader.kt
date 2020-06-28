package no.ntnu.ihb.acco.chart

import no.ntnu.ihb.acco.chart.jaxb.TChartConfig
import no.ntnu.ihb.acco.chart.jaxb.TLinearTransformation
import no.ntnu.ihb.acco.chart.jaxb.TTimeSeriesChart
import no.ntnu.ihb.acco.chart.jaxb.TXYSeriesChart
import no.ntnu.ihb.acco.core.LinearTransform
import java.io.File
import javax.xml.bind.JAXB

object ChartLoader {

    fun load(configFile: File): List<AbstractDrawer> {
        require(configFile.exists()) { "No such file $configFile" }
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
                else -> throw AssertionError("One of 'xyseries' or 'timeseries' required!")
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
                component.variable.forEach {
                    registerSeries(
                        VariableHandle(
                            component.name,
                            it.name,
                            it.linearTransformation?.convert()
                        )
                    )
                }
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
                    VariableHandle(it.x.component, it.x.variable),
                    VariableHandle(it.y.component, it.y.variable)
                )
            }
        }.build()
    }

}

private fun TLinearTransformation.convert(): LinearTransform {
    return LinearTransform(factor = factor, offset = offset)
}
