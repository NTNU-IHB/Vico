package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.vico.TestSsp
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File

internal class SimulateSspTest {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testSSP() {

        val resultDir = File("build/results").also {
            it.deleteRecursively()
        }

        val ssdFile = TestSsp.get("ControlledDrivetrain.ssp").absolutePath

        VicoCLI.main(
            arrayOf(
                "simulate-ssp",
                ssdFile,
                "--stepSize", "1e-3",
                "-rtf", "1.1",
                "-res", resultDir.absolutePath
            )
        )

        resultDir.apply {
            Assertions.assertTrue(exists())
            Assertions.assertEquals(3, listFiles()?.size)
        }

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testLogConfig() {

        val ssdFile = File(
            SimulateSspTest::class.java.classLoader
                .getResource("ssp/ControlledDrivetrain.ssp")!!.file
        ).absolutePath

        val resultDir = File("build/testLogConfig").also {
            it.deleteRecursively()
        }

        VicoCLI.main(
            arrayOf(
                "simulate-ssp",
                ssdFile,
                "--stepSize", "1e-3",
                "--stopTime", "2",
                "-res", resultDir.absolutePath,
                "-log", "extra/LogConfig.xml"
            )
        )

        resultDir.apply {
            Assertions.assertTrue(exists())
            Assertions.assertEquals(1, listFiles()?.size)
        }

    }

}
