package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.vico.TestFmus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class SimulateFmuTest {

    @Test
    fun testControlledTemperature() {

        val resultDir = File("build/results").also {
            it.deleteRecursively()
        }

        val fmuFile = TestFmus.get("2.0/ControlledTemperature.fmu").absolutePath

        VicoCLI.main(
            arrayOf(
                "simulate-fmu",
                fmuFile,
                "-stop", "1.0",
                "-dt", "0.01",
                "-res", resultDir.absolutePath
            )
        )

        resultDir.apply {
            Assertions.assertTrue(exists())
            Assertions.assertEquals(1, listFiles()?.size)
        }

    }

}
