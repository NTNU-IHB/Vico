package no.ntnu.ihb.vico.cli.commands

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.structure.SystemStructure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import kotlin.time.ExperimentalTime


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

    @CommandLine.Option(
        names = ["-chart", "--chartConfig"],
        description = ["Path to a chart configuration XML file. Path relative to the FMU"]
    )
    private var relativeChartConfigPath: String? = null

    @CommandLine.Option(
        names = ["-res", "--resultDir"],
        description = ["Directory to save the generated .csv file"]
    )
    private var resultDir: File = File("results")


    @CommandLine.Parameters(
        arity = "1",
        paramLabel = "FMU_FILE",
        description = ["Path to the FMU to simulate"]
    )
    private lateinit var fmu: File

    @ExperimentalTime
    override fun run() {

        val model = ModelResolver.resolve(fmu)
        val modelName = model.modelDescription.modelName
        val structure = SystemStructure().apply {
            addComponent(model, modelName)
        }

        require(startTime < stopTime) { "stopTime=$stopTime > startTime=$startTime!" }
        require(baseStepSize > 0) { "baseStepSize must be greater than 0" }

        Engine.Builder()
            .startTime(startTime)
            .stepSize(baseStepSize)
            .build().use { engine ->

                structure.apply(engine)

                relativeLogConfigPath?.also {
                    val logConfig = File(fmu.parent, it)
                    if (!logConfig.exists()) throw NoSuchFileException(logConfig)
                    engine.addSystem(SlaveLoggerSystem(logConfig, resultDir))
                } ?: run {
                    engine.addSystem(SlaveLoggerSystem(null, resultDir))
                }

                runSimulation(engine, startTime, stopTime, baseStepSize, targetRealtimeFactor, LOG)

            }

    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(SimulateFmu::class.java)
    }

}
