package no.ntnu.ihb.vico.cli.commands

import info.laht.krender.threekt.ThreektRenderer
import info.laht.kts.KtsScriptRunner
import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.chart.ChartLoader
import no.ntnu.ihb.vico.chart.ChartLoader2
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.dsl.ChartConfig
import no.ntnu.ihb.vico.dsl.ScenarioContext
import no.ntnu.ihb.vico.dsl.VisualConfig
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.render.TVisualConfig
import no.ntnu.ihb.vico.render.VisualLoader
import no.ntnu.ihb.vico.structure.SystemStructure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import javax.xml.bind.JAXB


@CommandLine.Command(name = "simulate-fmu", description = ["Simulate a single FMU"])
class SimulateFmu : Runnable {

    @CommandLine.Option(
        names = ["-start", "--startTime"],
        description = ["Time-point at which the simulation should start. Overrides any value specified in the FMU. Defaults to 0.0"]
    )
    private var startTime: Double = 0.0

    @CommandLine.Option(
        names = ["-stop", "--stopTime"],
        required = true,
        description = ["Time-point at which the simulation should stop. Required if not specified in the FMU. Overrides any value specified in the FMU."]
    )
    private var stopTime: Double = -1.0

    @CommandLine.Option(
        names = ["-dt", "--stepSize"],
        required = true,
        description = ["BaseStepSize for the FixedStepAlgorithm used."]
    )
    private var baseStepSize: Double = -1.0

    @CommandLine.Option(
        names = ["-rtf", "--realTimeFactor"],
        description = ["Target real time factor of the simulation. Defaults to unbounded."]
    )
    private var targetRealtimeFactor: Double? = null

    @CommandLine.Option(
        names = ["-l", "--logConfig"],
        description = ["Path to a log configuration XML file. Path relative to the FMU"]
    )
    private var relativeLogConfigPath: String? = null

    @CommandLine.Option(names = ["--no-log"], description = ["Don't enable variable logging."])
    private var disableVariableLogging = false

    @CommandLine.Option(
        names = ["-chart", "--chartConfig"],
        description = ["Path to a chart configuration file. Path relative to the FMU"]
    )
    private var relativeChartConfigPath: String? = null

    @CommandLine.Option(
        names = ["-visual", "--visualConfig"],
        description = ["Path to a visual configuration XML file. Path relative to the .ssd"]
    )
    private var relativeVisualConfigPath: String? = null

    @CommandLine.Option(
        names = ["-s", "--scenario"],
        description = ["Path to a scenario script. Path relative to the FMU"]
    )
    private var relativeScenarioConfig: String? = null

    @CommandLine.Option(
        names = ["-res", "--resultDir"],
        description = ["Directory to save the generated .csv file"]
    )
    private var resultDir: File = File("results")

    @CommandLine.Option(
        names = ["-port"],
        description = ["Enable the web server the given port"]
    )
    val port: Int? = null

    @CommandLine.Option(
        names = ["--paused"],
        description = ["Start simulation paused."]
    )
    private var paused = false

    @CommandLine.Option(
        names = ["--use-legacy-visuals"],
        description = ["Display legacy 3D graphics on dekstop."]
    )
    private var useLegacyVisuals = false


    @CommandLine.Option(
        names = ["--script-cache"],
        description = ["Cache directory for Kotlin scripts."]
    )
    private var scriptCache: File? = null

    @CommandLine.Parameters(
        arity = "1",
        paramLabel = "FMU_FILE",
        description = ["Path to the FMU to simulate"]
    )
    private lateinit var fmu: File

    override fun run() {

        require(baseStepSize > 0) { "baseStepSize must be greater than 0, was $baseStepSize.." }
        require(startTime < stopTime) { "stopTime=$stopTime > startTime=$startTime!" }

        val model = ModelResolver.resolve(fmu)
        val modelName = model.modelDescription.modelName
        val structure = SystemStructure().apply {
            addComponent(model, modelName)
        }

        val scriptEngine by lazy {
            KtsScriptRunner(scriptCache)
        }

        Engine.Builder()
            .startTime(startTime)
            .stepSize(baseStepSize)
            .build().use { engine ->

                if (!disableVariableLogging) {
                    relativeLogConfigPath?.also { configPath ->
                        var configFile = getConfigPath(fmu, configPath)
                        if (!configFile.exists()) configFile = File(configPath).absoluteFile
                        if (!configFile.exists()) throw NoSuchFileException(configFile)
                        engine.addSystem(SlaveLoggerSystem(configFile, resultDir))
                    } ?: run {
                        //log all variables
                        LOG.info("No log configuration provide, logging all variables..")
                        engine.addSystem(SlaveLoggerSystem(null, resultDir))
                    }
                }

                relativeChartConfigPath?.also { configPath ->
                    var configFile = getConfigPath(fmu, configPath)
                    if (!configFile.exists()) configFile = File(configPath).absoluteFile
                    if (!configFile.exists()) throw NoSuchFileException(configFile)
                    when (configFile.extension) {
                        "xml" -> {
                            ChartLoader.load(configFile).forEach { chart ->
                                engine.addSystem(chart)
                            }
                        }
                        "kts" -> {
                            @Suppress("UNCHECKED_CAST")
                            (scriptEngine.eval(configFile) as List<ChartConfig>?)?.also {
                                ChartLoader2.load(it).forEach { chart ->
                                    engine.addSystem(chart)
                                }
                            }
                        }
                    }
                }

                structure.apply(engine, FixedStepMaster(false))

                relativeScenarioConfig?.also { configPath ->
                    var configFile = getConfigPath(fmu, configPath)
                    if (!configFile.exists()) configFile = File(configPath).absoluteFile
                    if (!configFile.exists()) throw NoSuchFileException(configFile)
                    val scenario = (scriptEngine.eval(configFile) as ScenarioContext?)
                        ?: throw RuntimeException("Failed to load scenario!")
                    scenario.applyScenario(engine)
                }

                port?.also {
                    engine.addSystem(KtorServer(it))
                }

                relativeVisualConfigPath?.also { configPath ->
                    var configFile = getConfigPath(fmu, configPath)
                    if (!configFile.exists()) configFile = File(configPath).absoluteFile
                    if (!configFile.exists()) throw NoSuchFileException(configFile)
                    when (configFile.extension) {
                        "xml" -> {
                            val visualConfig = JAXB.unmarshal(configFile, TVisualConfig::class.java)
                            VisualLoader.load(visualConfig, engine)
                        }
                        "kts" -> {
                            @Suppress("UNCHECKED_CAST")
                            (scriptEngine.eval(configFile) as VisualConfig?)?.also { config ->
                                config.applyConfiguration(engine)
                            }
                        }
                    }
                    if (useLegacyVisuals) {
                        engine.addSystem(ThreektRenderer())
                    }
                }

                runSimulation(engine, startTime, stopTime, baseStepSize, targetRealtimeFactor, paused, LOG)

            }

    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(SimulateFmu::class.java)
    }

}
