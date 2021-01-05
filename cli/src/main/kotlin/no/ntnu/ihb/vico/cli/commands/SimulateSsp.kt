package no.ntnu.ihb.vico.cli.commands

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.chart.ChartLoader
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.render.VisualLoader
import no.ntnu.ihb.vico.scenario.parseScenario
import no.ntnu.ihb.vico.ssp.SSPLoader
import no.ntnu.ihb.vico.structure.SystemStructure
import org.joml.Matrix4f
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

    @CommandLine.Option(names = ["--no-log"], description = ["Don't enable variable logging."])
    private var disableLogging = false

    @CommandLine.Option(
        names = ["--no-parallel"],
        description = ["Don't enable variable parallel execution (if possible)."]
    )
    private var noParallel = false

    @CommandLine.Option(
        names = ["-chart", "--chartConfig"],
        description = ["Path to a chart configuration XML file. Path relative to the .ssd"]
    )
    private var relativeChartConfigPath: String? = null

    @CommandLine.Option(
        names = ["-visual", "--visualConfig"],
        description = ["Path to a visual configuration XML file. Path relative to the .ssd"]
    )
    private var relativeVisualConfigPath: String? = null

    @CommandLine.Option(
        names = ["-s", "--scenario"],
        description = ["Path to a scenario script. Path relative to the .ssd"]
    )
    private var relativeScenarioConfig: String? = null

    @CommandLine.Option(
        names = ["-p", "--parameterSet"],
        description = ["Name of parameterSet to load"]
    )
    private var parameterSet: String? = null

    @CommandLine.Option(
        names = ["-res", "--resultDir"],
        description = ["Directory to save the generated .csv file(s)"]
    )
    private var resultDir: File = File("results")

    @CommandLine.Parameters(
        arity = "1",
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

        val renderer = relativeVisualConfigPath?.let {
            ThreektRenderer().apply {
                setCameraTransform(Matrix4f().setTranslation(50f, 50f, 50f))
            }
        }

        Engine.Builder()
            .startTime(start)
            .stepSize(baseStepSize)
            .renderer(renderer)
            .build().use { engine ->

                if (!disableLogging) {
                    relativeLogConfigPath?.also { configPath ->
                        var config = getConfigPath(loader.ssdFile.parentFile, configPath)
                        if (!config.exists()) config = File(configPath).absoluteFile
                        if (!config.exists()) throw NoSuchFileException(config)
                        engine.addSystem(SlaveLoggerSystem(config, resultDir))
                    } ?: run {
                        //log all variables
                        engine.addSystem(SlaveLoggerSystem(null, resultDir))
                    }
                }

                relativeChartConfigPath?.also { configPath ->
                    var config = getConfigPath(loader.ssdFile.parentFile, configPath)
                    if (!config.exists()) config = File(configPath).absoluteFile
                    if (!config.exists()) throw NoSuchFileException(config)
                    ChartLoader.load(config).forEach { chart ->
                        engine.addSystem(chart)
                    }
                }

                val algorithm = FixedStepMaster(!noParallel)
                structure.apply(engine, algorithm, parameterSet)

                relativeScenarioConfig?.also { configPath ->
                    var config = getConfigPath(loader.ssdFile.parentFile, configPath)
                    if (!config.exists()) config = File(configPath).absoluteFile
                    if (!config.exists()) throw NoSuchFileException(config)
                    val scenario = parseScenario(config) ?: throw RuntimeException("Failed to load scenario!")
                    scenario.applyScenario(engine)
                }

                relativeVisualConfigPath?.also { configPath ->
                    var config = getConfigPath(loader.ssdFile.parentFile, configPath)
                    if (!config.exists()) config = File(configPath).absoluteFile
                    if (!config.exists()) throw NoSuchFileException(config)
                    VisualLoader.load(config, engine)
                }

                runSimulation(engine, start, stop, baseStepSize, targetRealtimeFactor, LOG)

            }

    }

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(SimulateSsp::class.java)

    }

}
