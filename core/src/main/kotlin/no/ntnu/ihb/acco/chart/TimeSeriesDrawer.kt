package no.ntnu.ihb.acco.chart

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.RealModifier
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

        seriesInfos.forEach {

            val (entityName, variableName, modifier) = it

            if (entityName == entity.name) {

                val variable = entity.getRealPropertyOrNull(it.variableName)
                    ?: throw IllegalArgumentException("No variable of type REAL named ${it.variableName} found in $entityName")

                val yProvider = ValueProvider {
                    val value = variable.read(DoubleArray(variable.size))[0]
                    modifier?.invoke(value) ?: value
                }

                handles["$entityName.$variableName"] = yProvider

            }

        }

    }

    override fun entityRemoved(entity: Entity) {

        seriesInfos.forEach {
            val (entityName, variableName) = it
            if (entityName == entity.name) {
                val key = "$entityName.$variableName"
                handles.remove(key)
            }
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
                val (timeData, yData) = data.getValue(handle.key)
                timeData.add(currentTime)
                yData.add(handle.value.get())

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
