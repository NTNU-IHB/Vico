package info.laht.acco.fmi

import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariable
import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SlaveLogger(
    private val targetDir: File = File(".")
) : Closeable {

    var separator = ", "
    var decimalPoints = 6
    private val loggers: MutableMap<SlaveComponent, Logger> = mutableMapOf()

    fun postInit(slave: SlaveComponent, currentTime: Double, numStep: Long) {
        loggers.computeIfAbsent(slave) {
            Logger(slave, emptyList()).also {
                it.writeHeader()
                it.writeLine(currentTime)
            }
        }
    }

    fun postStep(slave: SlaveComponent, currentTime: Double, numStep: Long) {
        loggers[slave]?.also {
            TODO()
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
        }

        fun writeHeader() {
            variables.joinToString(separator, "time, stepNumber, ", "\n") {
                "${it.name} [${it.valueReference} ${it.type} ${it.causality}]"
            }.also {
                writer.write(it)
            }
        }

        fun writeLine(currentTime: Double) {
            TODO()
        }

        override fun close() {
            writer.flush()
            writer.close()
        }
    }

}
