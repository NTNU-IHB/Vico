package no.ntnu.ihb.acco.chart

import no.ntnu.ihb.acco.core.Event
import no.ntnu.ihb.acco.core.EventSystem
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.Properties
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
import java.util.function.Supplier
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.properties.Delegates

internal typealias ValueProvider = Supplier<Double>

abstract class AbstractDrawer(
    private val title: String,
    xLabel: String,
    yLabel: String,
    width: Int,
    height: Int,
    live: Boolean,
    private val decimationFactor: Long
) : EventSystem(Family.all().build()) {

    protected val mutex = Unit
    private var queue: BlockingQueue<Unit>? = null
    protected val data: MutableMap<String, Pair<MutableList<Double>, MutableList<Double>>> = mutableMapOf()

    private var lastUpdate: Long = 0L

    var live: Boolean by Delegates.observable(live, { _, _, _ ->
        check(!initialized)
    })

    private val chart: XYChart by lazy {
        XYChartBuilder()
            .title(title)
            .xAxisTitle(xLabel)
            .yAxisTitle(yLabel)
            .width(width).height(height)
            .build().apply {
                styler.legendPosition = Styler.LegendPosition.InsideNE
            }
    }

    private val sw: SwingWrapper<XYChart> by lazy {
        SwingWrapper(chart)
    }

    init {
        listen(Properties.PROPERTIES_CHANGED)
    }


    override fun init(currentTime: Double) {
        updateData(currentTime)
        if (live) {
            display()
        }
    }

    override fun eventReceived(evt: Event) {
        when (evt.type) {
            Properties.PROPERTIES_CHANGED -> {
                postSlaveStep()
            }
        }
    }

    private fun postSlaveStep() {
        if (engine.iterations % decimationFactor == 0L) {

            updateData(engine.currentTime)

            if (live && (System.currentTimeMillis() - lastUpdate) > 100L) {
                updateChart()
                lastUpdate = System.currentTimeMillis()
            }

        }
    }

    override fun close() {
        if (!live) {
            display()
        } else {
            updateChart()
        }
        queue?.also {
            LOG.info("Waiting for chart '$title' to close..")
            it.take()
            LOG.info("Chart '$title' closed.")
        }
    }

    protected abstract fun updateData(currentTime: Double)

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

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractDrawer::class.java)
    }

    abstract class Builder<out E : AbstractDrawer>(
        protected val title: String,
        protected val xLabel: String,
        protected val yLabel: String
    ) {

        protected var live = false
        protected var width: Int = 600
        protected var height: Int = 400
        protected var decimationFactor = 10

        fun isLive(flag: Boolean) = apply {
            this.live = flag
        }

        fun decimationFactor(value: Int) = apply {
            this.decimationFactor = value
        }

        fun width(width: Int) = apply {
            this.width = width
        }

        fun height(height: Int) = apply {
            this.height = height
        }

        abstract fun build(): E
    }

}
