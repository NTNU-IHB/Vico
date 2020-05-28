package no.ntnu.ihb.vico.log

import no.ntnu.ihb.vico.log.jaxb.TComponent
import no.ntnu.ihb.vico.log.jaxb.TComponents
import no.ntnu.ihb.vico.log.jaxb.TLogConfig
import no.ntnu.ihb.vico.log.jaxb.TVariable

class LogConfigBuilder {

    private val map: MutableMap<String, Pair<Int, MutableList<String>>> = mutableMapOf()

    fun add(componentName: String, vararg variableNames: String): LogConfigBuilder {
        return add(componentName, 1, *variableNames)
    }

    fun add(componentName: String, decimationFactor: Int, vararg variableNames: String): LogConfigBuilder {
        return this.also {
            map.computeIfAbsent(componentName) { decimationFactor to mutableListOf() }.second.addAll(variableNames)
        }
    }

    fun build(): TLogConfig {
        return TLogConfig().also {
            it.components = TComponents().apply {
                map.forEach { (componentName, options) ->
                    component.add(TComponent().apply {
                        name = componentName
                        decimationFactor = options.first
                        variable.addAll(options.second.map {
                            TVariable().apply { name = it }
                        })
                    })
                }
            }
        }
    }

}
