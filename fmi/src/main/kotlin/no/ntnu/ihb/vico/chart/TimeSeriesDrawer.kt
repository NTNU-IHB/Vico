package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.RealModifier
import no.ntnu.ihb.fmi4j.readReal
import no.ntnu.ihb.vico.SlaveComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TimeSeriesDrawer internal constructor(
    title: String,
    label: String,
    width: Int,
    height: Int,
    live: Boolean,
    decimationFactor: Long,
    private val maxDuration: Double?,
    private val seriesInfos: MutableList<VariableHandle>,
    private val handles: MutableMap<String, ValueProvider>
) : AbstractDrawer(title, "Time[s]", label, width, height, live, decimationFactor) {

    class Builder(
        title: String,
        label: String
    ) : AbstractDrawer.Builder<TimeSeriesDrawer>(title, "", label) {

        private var maxDuration: Double? = null
        private val handles = mutableMapOf<String, ValueProvider>()
        private var seriesInfo = mutableListOf<VariableHandle>()

        fun maxDuration(value: Number?) = apply {
            value?.also {
                require(value.toDouble() > 0)
            }
            this.maxDuration = value?.toDouble()
        }

        fun registerSeries(componentName: String, variableName: String, modifier: RealModifier) = apply {
            registerSeries(
                VariableHandle(
                    componentName,
                    variableName,
                    modifier
                )
            )
        }

        fun registerSeries(componentName: String, variableName: String, vararg additionalVariableNames: String) =
            apply {
                registerSeries(
                    VariableHandle(
                        componentName,
                        variableName
                    )
                )
                for (additionalVariableName in additionalVariableNames) {
                    registerSeries(
                        VariableHandle(
                            componentName,
                            additionalVariableName
                        )
                    )
                }
            }

        fun registerSeries(componentName: String, variableNames: List<String>) = apply {
            require(variableNames.isNotEmpty())
            for (additionalVariableName in variableNames) {
                registerSeries(
                    VariableHandle(
                        componentName,
                        additionalVariableName
                    )
                )
            }
        }

        fun registerSeries(variableHandle: VariableHandle) = apply {
            seriesInfo.add(variableHandle)
        }

        fun registerSeries(name: String, yProvider: ValueProvider) = apply {
            handles[name] = yProvider
        }

        override fun build(): TimeSeriesDrawer {
            check(handles.isNotEmpty() || seriesInfo.isNotEmpty()) { "No series has been added using 'registerSeries'" }
            return TimeSeriesDrawer(
                title,
                yLabel,
                width,
                height,
                live,
                decimationFactor.toLong(),
                maxDuration,
                seriesInfo,
                handles
            )
        }

    }

    override fun entityAdded(entity: Entity) {

        val slave = entity.getComponent(SlaveComponent::class.java)
        val invalidVariableIdentifiers = mutableListOf<VariableHandle>()
        seriesInfos.forEach {

            val (componentName, variableName, modifier) = it


            val variable = slave.modelDescription.getVariableByName(variableName)
            slave.markForReading(variable.name)

            val yProvider: ValueProvider = {
                val value = slave.readReal(variable.valueReference).value
                modifier?.invoke(value) ?: value
            }
            handles["$componentName.$variableName"] = yProvider

        }

        seriesInfos.removeAll(invalidVariableIdentifiers)

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
                val (timeData, yData) = data.getValue(handle.key)
                timeData.add(currentTime)
                yData.add(handle.value.invoke())

                if (maxDuration != null && timeData.size >= 2) {
                    while ((timeData.last() - timeData.first()) > maxDuration) {
                        timeData.removeAt(0)
                        yData.removeAt(0)
                    }
                }

            }
        }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(TimeSeriesDrawer::class.java)
    }

}
