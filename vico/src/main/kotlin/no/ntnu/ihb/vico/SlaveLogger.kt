package no.ntnu.ihb.vico

import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.VariableType
import no.ntnu.ihb.fmi4j.readBoolean
import no.ntnu.ihb.fmi4j.readInteger
import no.ntnu.ihb.fmi4j.readReal
import no.ntnu.ihb.fmi4j.readString
import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SlaveLogger(
    targetDir: File? = null
) : Closeable {

    var separator: String = ", "
    var decimalPoints: Int = 6
    val targetDir: File = targetDir ?: File(".")
    private val loggers: MutableMap<SlaveComponent, Logger> = mutableMapOf()

    init {
        this.targetDir.mkdirs()
    }

    fun setup(slave: SlaveComponent) {
        loggers[slave] = Logger(slave, emptyList()).also {
            it.writeHeader()
        }
    }

    fun postInit(slave: SlaveComponent, currentTime: Double) {
        loggers[slave]?.also {
            it.writeLine(currentTime)
        }
    }

    fun postStep(slave: SlaveComponent, currentTime: Double) {
        loggers[slave]?.also {
            it.writeLine(currentTime)
        }
    }

    override fun close() {
        loggers.values.forEach { it.close() }
    }

    private inner class Logger(
        private val slave: SlaveComponent,
        variables: List<ScalarVariable>,
        staticFileNames: Boolean = false
    ) : Closeable {

        private val writer: BufferedWriter
        private val variables: List<ScalarVariable>

        init {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = if (staticFileNames) slave.instanceName else "${slave.instanceName}_$dateFormat"
            this.writer = FileOutputStream(File(targetDir, "${fileName}.csv")).bufferedWriter()
            this.variables = if (variables.isEmpty()) slave.modelVariables.toList() else variables
            this.variables.forEach {
                slave.markForReading(it.name)
            }
        }

        fun writeHeader() {
            variables.joinToString(separator, "time, stepNumber, ", "\n") {
                "${it.name} [${it.valueReference} ${it.type} ${it.causality}]"
            }.also {
                writer.write(it)
            }
        }

        @Suppress("IMPLICIT_CAST_TO_ANY")
        fun writeLine(currentTime: Double) {
            variables.map {
                when (it.type) {
                    VariableType.INTEGER, VariableType.ENUMERATION -> slave.readInteger(it.valueReference).value
                    VariableType.REAL -> slave.readReal(it.valueReference).value.formatForOutput(decimalPoints)
                    VariableType.BOOLEAN -> slave.readBoolean(it.valueReference).value
                    VariableType.STRING -> slave.readString(it.valueReference).value
                }
            }.joinToString(
                separator,
                "${currentTime.formatForOutput(decimalPoints)}$separator${slave.stepCount}$separator",
                "\n"
            ).also {
                writer.write(it)
            }
        }

        override fun close() {
            writer.flush()
            writer.close()
        }

        private fun Double.formatForOutput(decimalPoints: Int = 4): String {
            return String.format(Locale.US, "%.${decimalPoints}f", this)
        }

    }

}
