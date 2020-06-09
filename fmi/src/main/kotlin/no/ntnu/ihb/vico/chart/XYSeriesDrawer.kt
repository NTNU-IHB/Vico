package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.fmi4j.readReal
import no.ntnu.ihb.vico.SlaveSystem
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
    private val seriesInfos: MutableMap<String, Pair<VariableIdentifier, VariableIdentifier>>
) : AbstractDrawer(title, xLabel, yLabel, width, height, live, decimationFactor) {

    class Builder(
        title: String,
        xLabel: String,
        yLabel: String
    ) : AbstractDrawer.Builder<XYSeriesDrawer>(title, xLabel, yLabel) {

        private var maxLength: Int? = null
        private val handles = mutableMapOf<String, Pair<ValueProvider, ValueProvider>>()
        private var seriesInfo = mutableMapOf<String, Pair<VariableIdentifier, VariableIdentifier>>()

        fun maxLength(value: Int?) = apply {
            value?.also {
                require(value > 0)
            }
            this.maxLength = value
        }

        fun registerSeries(name: String, xProvider: ValueProvider, yProvider: ValueProvider) = apply {
            handles[name] = xProvider to yProvider
        }

        fun registerSeries(name: String, x: VariableIdentifier, y: VariableIdentifier) = apply {
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

    override fun assignedToEngine(engine: Engine) {

        fun fail(key: String, component: String) {
            LOG.warn("Failed to add xyseries as no component named '${component}' exists!")
            seriesInfos.remove(key)
        }

        val system = engine.getSystem(SlaveSystem::class.java)

        seriesInfos.forEach { (key, value) ->

            system.getSlaveNoExcept(value.first.componentName)?.also { slave1 ->

                val variable1 = slave1.modelDescription.getVariableByName(value.first.variableName)
                slave1.markForReading(variable1.name)

                system.getSlaveNoExcept(value.second.componentName)?.also { slave2 ->

                    val variable2 = slave2.modelDescription.getVariableByName(value.second.variableName)
                    slave2.markForReading(variable2.name)

                    val xProvider: ValueProvider = { slave1.readReal(variable1.valueReference).value }
                    val yProvider: ValueProvider = { slave2.readReal(variable2.valueReference).value }
                    handles[key] = xProvider to yProvider

                } ?: fail(key, value.second.componentName)

            } ?: fail(key, value.first.componentName)
        }
    }

    override fun init(currentTime: Double) {
        handles.forEach { handle ->
            data[handle.key] = mutableListOf<Double>() to mutableListOf()
        }
        super.init(currentTime)
    }

    override fun updateData(currentTime: Double) {
        synchronized(mutex) {
            handles.forEach { handle ->

                val (xData, yData) = data.getValue(handle.key)
                xData.add(handle.value.first.invoke())
                yData.add(handle.value.second.invoke())

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

