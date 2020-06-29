package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.vico.TestSsp
import org.junit.jupiter.api.Assertions
import java.io.File

fun main() {

    val resultDir = File("build/results/QuarterTruck").also {
        Assertions.assertTrue(it.deleteRecursively())
    }

    val ssdFile = TestSsp.get("quarter-truck")

    VicoCLI.main(
        arrayOf(
            "simulate-ssp",
            ssdFile.absolutePath,
            "--stepSize", "1e-2",
            "--stopTime", "10",
            //"-rtf", "1.0",
            "-log", "LogConfig_vico.xml",
            "-chart", "ChartConfig.xml",
            "-res", resultDir.absolutePath
        )
    )

}

