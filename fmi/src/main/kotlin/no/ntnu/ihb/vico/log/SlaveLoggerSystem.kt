package no.ntnu.ihb.vico.log

import no.ntnu.ihb.acco.core.*
import no.ntnu.ihb.acco.core.Properties
import no.ntnu.ihb.acco.util.formatForOutput
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.log.jaxb.TLogConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.bind.JAXB

class SlaveLoggerSystem(
    private val logConfig: TLogConfig? = null,
    targetDir: File? = null
) : EventSystem(Family.all(SlaveComponent::class.java).build()) {

    var separator: String = ", "
    var decimalPoints: Int = 6
    val targetDir: File = targetDir ?: File(".")
    private val loggers: MutableMap<String, SlaveLogger> = mutableMapOf()

    constructor(configFile: File, targetDir: File? = null) : this(
        JAXB.unmarshal(configFile, TLogConfig::class.java), targetDir
    )

    init {

        priority = Int.MAX_VALUE

        this.targetDir.mkdirs()

        logConfig?.components?.component?.also {
            mutableSetOf<String>().apply {
                for (key in it) {
                    check(add(key.name)) { "Duplicate component in log configuration: '${key.name}'" }
                }
            }
        }

        listen(Properties.PROPERTIES_CHANGED)
    }

    override fun entityAdded(entity: Entity) {
        val slave = entity.getComponent<SlaveComponent>()
        if (logConfig == null) {
            loggers[slave.instanceName] = SlaveLogger(slave, emptyList(), 1)
        } else {
            val logInfo = logConfig.components?.component?.associateBy { it.name } ?: mutableMapOf()
            logInfo[slave.instanceName]?.also { component ->
                val decimationFactor = component.decimationFactor
                require(decimationFactor >= 1)
                val variables = component.variable.map { v ->
                    slave.getProperty(v.name)!!
                }
                loggers[slave.instanceName] =
                    SlaveLogger(slave, variables, decimationFactor, logConfig.isStaticFileNames)
            }
        }
        loggers[slave.instanceName]?.also {
            it.writeHeader()
        }
    }

    override fun entityRemoved(entity: Entity) {
        loggers.remove(entity.name)?.also {
            it.close()
        }
    }

    override fun init(currentTime: Double) {
        loggers.values.forEach { logger ->
            logger.writeLine(currentTime)
        }
    }

    override fun eventReceived(evt: Event) {
        when (evt.type) {
            Properties.PROPERTIES_CHANGED -> {
                val (currentTime, slave) = evt.target<Pair<Double, SlaveComponent>>()
                loggers[slave.instanceName]?.also { logger ->
                    logger.writeLine(currentTime)
                }
            }
        }
    }

    override fun close() {
        loggers.values.forEach { it.close() }
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(SlaveLoggerSystem::class.java)
    }

    private inner class SlaveLogger(
        private val slave: SlaveComponent,
        variables: List<Property>,
        private val decimationFactor: Int,
        staticFileNames: Boolean = false
    ) : Closeable {

        private val writer: BufferedWriter
        private val variables: Collection<Property>

        init {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = if (staticFileNames) slave.instanceName else "${slave.instanceName}_$dateFormat"
            this.writer = FileOutputStream(File(targetDir, "${fileName}.csv")).bufferedWriter()
            this.variables = if (variables.isEmpty()) slave.getProperties() else variables
        }

        fun writeHeader() {
            variables.joinToString(separator, "time, stepNumber, ", "\n") {
                val causality = it.causality
                "${it.name} [${it.type} $causality]"
            }.also {
                writer.write(it)
            }
        }

        @Suppress("IMPLICIT_CAST_TO_ANY")
        fun writeLine(currentTime: Double) {
            if (slave.stepCount % decimationFactor == 0L) {
                variables.map {
                    when (it) {
                        is IntProperty -> it.read().first()
                        is RealProperty -> it.read().first().formatForOutput(decimalPoints)
                        is StrProperty -> it.read().first()
                        is BoolProperty -> it.read().first()
                    }
                }.joinToString(
                    separator,
                    "${currentTime.formatForOutput(decimalPoints)}$separator${slave.stepCount}$separator",
                    "\n"
                ).also {
                    writer.write(it)
                }
            }
        }

        override fun close() {
            writer.flush()
            writer.close()
            LOG.debug("Wrote csv data for slave '${slave.instanceName} to folder '${targetDir.absolutePath}'")
        }

    }

}
