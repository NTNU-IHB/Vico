package no.ntnu.ihb.vico.cli.commands

import no.ntnu.ihb.acco.chart.ChartLoader
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.ssp.SSPLoader
import no.ntnu.ihb.vico.structure.SystemStructure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import kotlin.time.ExperimentalTime

@CommandLine.Command(
    name = "simulate-ssp",
    description = ["Simulate a co-simulation system described using SSP"]
)
class SimulateSsp : Runnable {

    @CommandLine.Option(
        names = ["-start", "--startTime"],
        description = ["Time-point at which the simulation should start. Overrides any value specified in the SSP. Defaults to 0.0"]
    )
    private var startTime: Double? = null

    @CommandLine.Option(
        names = ["-stop", "--stopTime"],
        description = ["Time-point at which the simulation should stop. Required if not specified in the SSP. Overrides any value specified in the SSP."]
    )
    private var stopTime: Double? = null

    @CommandLine.Option(
        names = ["-dt", "--stepSize"],
        description = ["BaseStepSize for the FixedStepAlgorithm. Required if an algorithm is not specified in the SSP. Overrides any algorithm specified in the SSP."]
    )
    private var baseStepSize: Double = -1.0

    @CommandLine.Option(
        names = ["-rtf", "--realTimeFactor"],
        description = ["Target real time factor of the simulation. Defaults to unbounded."]
    )
    private var targetRealtimeFactor: Double? = null

    @CommandLine.Option(
        names = ["-log", "--logConfig"],
        description = ["Path to a log configuration XML file. Path relative to the .ssd"]
    )
    private var relativeLogConfigPath: String? = null

    @CommandLine.Option(
        names = ["-chart", "--chartConfig"],
        description = ["Path to a chart configuration XML file. Path relative to the .ssd"]
    )
    private var relativeChartConfigPath: String? = null

    @CommandLine.Option(
        names = ["-res", "--resultDir"],
        description = ["Directory to save the generated .csv file(s)"]
    )
    private var resultDir: File = File("results")

    @CommandLine.Parameters(
        arity = "1..*",
        paramLabel = "SSP_CONFIG",
        description = ["Path to a either a .ssp file, a .ssd file or a directory containing a file named SystemStructure.ssd"]
    )
    private lateinit var sspFile: File

    @ExperimentalTime
    override fun run() {

        require(baseStepSize > 0) { "baseStepSize must be greater than 0" }

        val loader = SSPLoader(sspFile)
        val structure: SystemStructure = loader.load()
        val start: Double = startTime ?: structure.defaultExperiment?.startTime ?: 0.0
        val stop = stopTime ?: structure.defaultExperiment?.stopTime
        ?: throw Error("No stopTime provided as input, nor has one been specified in the SSP!")

        require(start < stop) { "stop=$stop > start=$start!" }

        Engine(baseStepSize).use { engine ->

            structure.apply(engine)

            relativeLogConfigPath?.also { configPath ->
                val logConfig = File(loader.ssdFile.parent, configPath)
                if (!logConfig.exists()) throw NoSuchFileException(logConfig)
                engine.addSystem(SlaveLoggerSystem(logConfig, resultDir))
            } ?: run {
                engine.addSystem(SlaveLoggerSystem(null, resultDir))
            }

            relativeChartConfigPath?.also { configPath ->
                val chartConfig = File(loader.ssdFile.parent, configPath)
                if (!chartConfig.exists()) throw NoSuchFileException(chartConfig)
                ChartLoader.load(chartConfig).forEach { chart ->
                    engine.addSystem(chart)
                }
            }

            runSimulation(engine, start, stop, baseStepSize, targetRealtimeFactor, LOG)

        }

    }

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(SimulateSsp::class.java)

    }

}
