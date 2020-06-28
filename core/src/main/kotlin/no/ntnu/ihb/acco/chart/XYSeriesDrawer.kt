package no.ntnu.ihb.acco.chart

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class XYSeriesDrawer internal constructor(
    title: String,
    xLabel: String,
    yLabel: String,
    width: Int,
    height: Int,
    live: Boolean,
    decimationFactor: Long,
    private val maxLength: Int?,
    private val handles: MutableMap<String, Pair<ValueProvider, ValueProvider>>,
    private val seriesInfos: MutableMap<String, Pair<VariableHandle, VariableHandle>>
) : AbstractDrawer(title, xLabel, yLabel, width, height, live, decimationFactor) {

    class Builder(
        title: String,
        xLabel: String,
        yLabel: String
    ) : AbstractDrawer.Builder<XYSeriesDrawer>(title, xLabel, yLabel) {

        private var maxLength: Int? = null
        private val handles: MutableMap<String, Pair<ValueProvider, ValueProvider>> = mutableMapOf()
        private var seriesInfo: MutableMap<String, Pair<VariableHandle, VariableHandle>> = mutableMapOf()

        fun maxLength(value: Int?) = apply {
            value?.also {
                require(value > 0)
            }
            this.maxLength = value
        }

        fun registerSeries(name: String, xProvider: ValueProvider, yProvider: ValueProvider) = apply {
            handles[name] = xProvider to yProvider
        }

        fun registerSeries(name: String, x: VariableHandle, y: VariableHandle) = apply {
            seriesInfo[name] = x to y
        }

        override fun build(): XYSeriesDrawer {
            check(handles.isNotEmpty() || seriesInfo.isNotEmpty()) { "No series has been added using 'registerSeries'" }
            return XYSeriesDrawer(
                title,
                xLabel,
                yLabel,
                width,
                height,
                live,
                decimationFactor.toLong(),
                maxLength,
                handles,
                seriesInfo
            )
        }

    }

    override fun init(currentTime: Double) {

        seriesInfos.forEach { (key, value) ->

            val (h1, h2) = value

            val variable1 = engine.getEntityByName(h1.componentName).getRealPropertyOrNull(h1.variableName)
                ?: throw IllegalStateException("No variable named '${h1.variableName}' in entity ${h1.componentName}")
            val variable2 = engine.getEntityByName(h2.componentName).getRealPropertyOrNull(h2.variableName)
                ?: throw IllegalStateException("No variable named '${h2.variableName}' in entity ${h2.componentName}")

            val xProvider = ValueProvider {
                variable1.read()[0]
            }
            val yProvider = ValueProvider {
                variable2.read()[0]
            }
            handles[key] = xProvider to yProvider
        }

        handles.forEach { handle ->
            data[handle.key] = mutableListOf<Double>() to mutableListOf()
        }
        super.init(currentTime)
    }

    override fun updateData(currentTime: Double) {
        synchronized(mutex) {
            handles.forEach { handle ->

                val (xData, yData) = data.getValue(handle.key)
                xData.add(handle.value.first.get())
                yData.add(handle.value.second.get())

                if (maxLength != null && xData.size > maxLength) {
                    xData.removeAt(0)
                    yData.removeAt(0)
                }

            }
        }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(XYSeriesDrawer::class.java)
    }

}

