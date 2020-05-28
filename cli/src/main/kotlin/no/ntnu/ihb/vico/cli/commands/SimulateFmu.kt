package no.ntnu.ihb.vico.cli.commands

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.HeadlessEngineRunner
import no.ntnu.ihb.acco.util.formatForOutput
import no.ntnu.ihb.vico.ModelResolver
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.log.SlaveLogger
import no.ntnu.ihb.vico.structure.SystemStructure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


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

        require(startTime < stopTime) { "stopTime=$stopTime > startTime=$startTime!" }
        require(baseStepSize > 0)

        Engine(baseStepSize).use { engine ->

            SystemStructure().apply {
                addComponent(model, modelName)
            }.apply(engine)

            val slaveSystem = engine.getSystem(SlaveSystem::class.java)

            relativeLogConfigPath?.also {
                val logConfig = File(fmu.parent, it)
                if (!logConfig.exists()) throw NoSuchFileException(logConfig)
                slaveSystem.addListener(SlaveLogger(logConfig, resultDir))
            } ?: run {
                slaveSystem.addListener(SlaveLogger(null, resultDir))
            }

            val runner = HeadlessEngineRunner(engine)

            val totalSimulationTime = stopTime - startTime
            val numSteps = (totalSimulationTime / baseStepSize).toLong()
            val aTenth = numSteps / 10

            targetRealtimeFactor?.also {
                runner.targetRealTimeFactor = it
            } ?: run {
                runner.enableRealTimeTarget = false
            }
            runner.callback = {
                val i = engine.iterations
                if (i != 0L && i % aTenth == 0L || i == numSteps) {
                    val percentComplete = i / numSteps.toDouble() * 100
                    LOG.info(
                        "{}% complete, simulated {}s in {}s, target RTF={}, actual RTF={}",
                        percentComplete,
                        runner.simulationClock.formatForOutput(),
                        runner.wallClock.formatForOutput(),
                        if (runner.enableRealTimeTarget) runner.targetRealTimeFactor else "unbounded",
                        runner.actualRealTimeFactor.formatForOutput()
                    )

                }
            }

            measureTime {
                runner.runUntil(stopTime).get()
            }.also { t ->
                LOG.info("Simulation finished. Simulated ${totalSimulationTime}s in ${t.inSeconds}s.. ")
            }

        }

    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(SimulateFmu::class.java)
    }

}
