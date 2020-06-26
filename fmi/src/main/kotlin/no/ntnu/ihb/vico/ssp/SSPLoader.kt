package no.ntnu.ihb.vico.ssp

import no.ntnu.ihb.acco.core.LinearTransform
import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import no.ntnu.ihb.fmi4j.util.extractContentTo
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.ssp.jaxb.SystemStructureDescription
import no.ntnu.ihb.vico.ssp.jaxb.TComponent
import no.ntnu.ihb.vico.structure.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Element
import java.io.File
import java.net.URI
import java.nio.file.Files
import javax.xml.bind.JAXB

private typealias Components = Map<String, Component>

private const val DEFAULT_SSD_FILENAME = "SystemStructure.ssd"
private const val VICO_NAMESPACE = "com.github.ntnu-ihb.vico"
private const val OSP_NAMESPACE = "com.opensimulationplatform"

class SSPLoader @JvmOverloads constructor(
    sspFile: File,
    customSsdName: String? = null
) {

    val ssdFile: File

    init {
        require(sspFile.exists()) { "No such file: '$sspFile'" }

        val systemStructureFile = customSsdName ?: DEFAULT_SSD_FILENAME

        this.ssdFile = when {
            sspFile.extension == "ssd" -> {
                sspFile
            }
            sspFile.extension == "ssp" -> {
                val temp = Files.createTempDirectory("vico_").toFile().also {
                    it.deleteOnExit()
                }
                sspFile.extractContentTo(temp)
                File(temp, systemStructureFile)
            }
            sspFile.isDirectory -> {
                File(sspFile, systemStructureFile)
            }
            else -> throw IllegalArgumentException("Unsupported input file: $sspFile")
        }

        require(this.ssdFile.exists()) { "No such file: '$sspFile'" }
    }

    fun load(): SystemStructure {

        val ssd = JAXB.unmarshal(ssdFile, SystemStructureDescription::class.java)
        val components = parseComponents(ssd)
        val structure = SystemStructure(ssd.name).apply {
            components.values.forEach { component ->
                addComponent(component)
            }
            parseConnections(ssd, components).forEach { connection ->
                addConnection(connection)
            }
            parseDefaultExperiment(ssd)?.also { defaultExperiment = it }
        }

        LOG.info("Loaded SSP config '${ssd.name}'")
        return structure
    }

    private fun parseComponents(ssd: SystemStructureDescription): Components {
        return ssd.system.elements.components.map { c ->
            parseComponent(c).also { component ->
                c.connectors?.connector?.also { sspConnectors ->
                    sspConnectors.forEach { sspConnector ->
                        val connector = when {
                            sspConnector.integer != null -> IntegerConnector(
                                sspConnector.name,
                                ConnectorKind.valueOf(sspConnector.kind.toUpperCase())
                            )
                            sspConnector.real != null -> RealConnector(
                                sspConnector.name,
                                ConnectorKind.valueOf(sspConnector.kind.toUpperCase()),
                                sspConnector.real.unit
                            )
                            sspConnector.boolean != null -> BooleanConnector(
                                sspConnector.name,
                                ConnectorKind.valueOf(sspConnector.kind.toUpperCase())
                            )
                            sspConnector.string != null -> StringConnector(
                                sspConnector.name,
                                ConnectorKind.valueOf(sspConnector.kind.toUpperCase())
                            )
                            sspConnector.binary != null -> {
                                throw UnsupportedOperationException("Binary connector is currently unsupported!")
                            }
                            sspConnector.enumeration != null -> {
                                throw UnsupportedOperationException("Enumeration connector is currently unsupported!")
                            }
                            else -> throw AssertionError()
                        }
                        component.addConnector(connector)
                    }
                }
                parseParameterBindings(c, component)
            }
        }.associateBy { it.instanceName }
    }

    private fun parseParameterBindings(c: TComponent, component: Component) {
        c.parameterBindings?.parameterBinding?.forEach { binding ->
            if (binding.source != null) {
                val parameterSet = JAXB.unmarshal(
                    URI("${ssdFile.parentFile.toURI()}/${binding.source}"),
                    no.ntnu.ihb.vico.ssp.jaxb.ParameterSet::class.java
                )
                component.addParameterSet(parseParameterSet(parameterSet))
            } else {
                binding.parameterValues.parameterSet.forEach { parameterSet ->
                    component.addParameterSet(parseParameterSet(parameterSet))
                }
            }
        }
    }

    private fun parseParameterSet(p: no.ntnu.ihb.vico.ssp.jaxb.ParameterSet): ParameterSet {
        val parameters = p.parameters.parameter.mapNotNull {
            when {
                it.integer != null -> IntegerParameter(it.name, it.integer.value)
                it.real != null -> RealParameter(it.name, it.real.value)
                it.boolean != null -> BooleanParameter(it.name, it.boolean.isValue)
                it.string != null -> StringParameter(it.name, it.string.value)
                it.enumeration != null -> {
                    LOG.error("Enumerations parameters are unsupported")
                    null
                }
                else -> throw UnsupportedOperationException("Unable to parse parameter: $it")
            }
        }
        return ParameterSet(p.name, parameters)
    }

    private fun parseComponent(c: TComponent): Component {
        var uri = URI(c.source)
        if (!uri.isAbsolute) {
            uri = URI("${ssdFile.parentFile.absoluteFile.toURI()}/${c.source}")
        }
        var stepSizeHint: Double? = null
        c.annotations?.annotation?.forEach { annotation ->
            when (annotation.type) {
                VICO_NAMESPACE, OSP_NAMESPACE -> {
                    val elem = annotation.any as Element
                    when (elem.nodeName) {
                        "vico:StepSizeHint, osp:StepSizeHint" -> {
                            stepSizeHint = elem.getAttribute("value").toDoubleOrNull()
                        }
                    }
                }
            }
        }
        val model = ModelResolver.resolve(ssdFile.parentFile, uri)
        return Component(model, c.name, stepSizeHint)
    }

    private fun parseConnections(
        ssd: SystemStructureDescription,
        components: Components
    ): List<Connection<*>> {
        return ssd.system.connections?.connection?.map { c ->

            val startComponent = components[c.startElement]
                ?: throw RuntimeException("No component named '${c.endElement}'")
            val startConnector = startComponent.getConnector(c.startConnector)

            val endComponent = components[c.endElement]
                ?: throw RuntimeException("No component named '${c.endElement}'")
            val endConnector = endComponent.getConnector(c.endConnector)

            val startVariable = startComponent.modelDescription.getVariableByName(startConnector.name)
            val endVariable = endComponent.modelDescription.getVariableByName(endConnector.name)

            require(startVariable.type == endVariable.type)

            when (startVariable.type) {
                VariableType.INTEGER, VariableType.ENUMERATION -> IntegerConnection(
                    startComponent, startVariable as IntegerVariable,
                    endComponent, endVariable as IntegerVariable
                )
                VariableType.REAL -> RealConnection(
                    startComponent, startVariable as RealVariable,
                    endComponent, endVariable as RealVariable
                ).also { realConnection ->
                    c.linearTransformation?.also { t ->
                        realConnection.modifiers.add(LinearTransform(t.factor, t.offset))
                    }
                }
                VariableType.STRING -> StringConnection(
                    startComponent, startVariable as StringVariable,
                    endComponent, endVariable as StringVariable
                )
                VariableType.BOOLEAN -> BooleanConnection(
                    startComponent, startVariable as BooleanVariable,
                    endComponent, endVariable as BooleanVariable
                )
            }

        } ?: emptyList()
    }

    /*private fun parseSystem(elem: Element): System {
        val algorithmNode = elem.firstChild
        if ("FixedStepAlgorithm" in algorithmNode.nodeName) {
            val baseStepSize = algorithmNode.attributes
                .getNamedItem("baseStepSize").nodeValue.toDouble()
            return SlaveSystem(baseStepSize)
        }
        throw UnsupportedOperationException("Unsupported algorithm: ${algorithmNode.nodeName}")
    }
    */
    private fun parseDefaultExperiment(ssd: SystemStructureDescription): DefaultExperiment? {
        return ssd.defaultExperiment?.let {
            val startTime = it.startTime ?: 0.0
            val stopTime: Double? = it.stopTime
            /*var system: System? = null
            it.annotations?.annotation?.forEach { annotation ->
                when (annotation.type.toLowerCase()) {
                    VICO_NAMESPACE, OSP_NAMESPACE -> {
                        val elem = annotation.any as Element
                        when (elem.nodeName) {
                            "vico:Algorithm", "osp:Algorithm" -> algorithm = parseAlgorithm(elem)
                        }
                    }
                }
            }*/
            DefaultExperiment(startTime, stopTime)
        }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(SSPLoader::class.java)
    }

}
