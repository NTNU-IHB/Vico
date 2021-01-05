package no.ntnu.ihb.vico.examples

import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.ssp.SSPLoader
import java.io.File

fun main() {

    val examplesFolder = File("examples/gunnerus/trajectory").absoluteFile
    val sspFile = File(examplesFolder, "gunnerus-trajectory.ssp")
    if (!sspFile.exists()) {
        throw NoSuchFileException(sspFile)
    }
    if (sspFile.exists()) {

        EngineBuilder().stepSize(0.05).build().use { engine ->

            SSPLoader(sspFile).load().apply(engine, parameterSet = "initialValues")

            engine.runner.apply {
                enableRealTimeTarget = false
                runForAndWait(100)
            }

        }


    }

}
