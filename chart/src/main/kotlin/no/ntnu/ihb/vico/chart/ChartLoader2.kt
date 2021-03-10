package no.ntnu.ihb.vico.chart


import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.ObserverSystem
import no.ntnu.ihb.vico.dsl.ChartConfig
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.SeriesMarkers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import javax.swing.JFrame
import javax.swing.SwingUtilities


object ChartLoader2 {

    fun load(config: List<ChartConfig>): List<ChartDrawer> {
        return config.map {
            ChartDrawer((it))
        }
    }

}

class ChartDrawer(
    private val config: ChartConfig
) : ObserverSystem(Family.all) {

    private val mutex = Unit
    private var queue: BlockingQueue<Unit>? = null
    private val data: MutableMap<String, Pair<MutableList<Double>, MutableList<Double>>> = mutableMapOf()

    private val chart: XYChart by lazy {
        XYChartBuilder()
            .title(config.title)
            .xAxisTitle(config.xLabel)
            .yAxisTitle(config.yLabel)
            .width(config.width).height(config.height)
            .build().apply {
                styler.legendPosition = Styler.LegendPosition.InsideNE
            }
    }

    private val sw: SwingWrapper<XYChart> by lazy {
        SwingWrapper(chart)
    }

    init {
        priority = Int.MAX_VALUE

        config.series.forEach {
            data[it.name] = mutableListOf<Double>() to mutableListOf<Double>()

        }
    }

    override fun postInit() {
        updateData()
    }

    override fun observe(currentTime: Double) {
        updateData()
    }

    private fun updateData() {
        config.series.forEach { series ->
            data[series.name]?.apply {
                first.add(series.xGetter.get(engine))
                second.add(series.yGetter.get(engine))
            } ?: throw IllegalStateException("No series with name '${series.name}' registered")
        }
    }

    private fun display() {

        data.forEach {
            val series = chart.addSeries(it.key, it.value.first, it.value.second, null)
            series.marker = SeriesMarkers.NONE
        }

        queue = ArrayBlockingQueue(1)
        sw.displayChart().apply {
            defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            addWindowListener(object : WindowAdapter() {
                override fun windowClosed(e: WindowEvent?) {
                    queue!!.add(Unit)
                }
            })
        }
    }

    private fun updateChart() {
        SwingUtilities.invokeLater {
            synchronized(mutex) {
                data.forEach {
                    chart.updateXYSeries(it.key, it.value.first, it.value.second, null)
                }
                sw.repaintChart()
            }
        }
    }

    override fun close() {
        if (!config.live) {
            display()
        } else {
            updateChart()
        }
        queue?.also {
            LOG.info("Waiting for chart '${config.title}' to close..")
            try {
                it.take()
            } catch (ex: InterruptedException) {
                //ignore
            }
            LOG.info("Chart '${config.title}' closed.")
        }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractDrawer::class.java)
    }

}
