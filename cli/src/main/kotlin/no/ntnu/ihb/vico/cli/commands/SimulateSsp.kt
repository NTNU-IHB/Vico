package no.ntnu.ihb.vico.cli.commands

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File

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
    private var baseStepSize: Double? = null

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

    override fun run() {

/*
            val loader = SSPLoader(sspFile)
            val structure: SystemStructure = loader.load()
            val start: Double = startTime ?: structure.defaultExperiment?.startTime ?: 0.0
            val stop = stopTime ?: structure.defaultExperiment?.stopTime
            ?: throw Error("No stopTime provided as input, nor has one been specified in the SSP!")
            val algorithm = baseStepSize?.let { FixedStepMaster(it) } ?: structure.defaultExperiment?.algorithm
            ?: throw Error("No baseStepSize provided as input, nor has an algorithm been specified in the SSP!")

            require(start < stop) { "stop > start!" }

            ExecutionBuilder(structure).apply {
                startTime(start)
                algorithm(algorithm)
            }.build().use { exec ->

                relativeLogConfigPath?.also { configPath ->
                    val logConfig = File(loader.ssdFile.parent, configPath)
                    if (!logConfig.exists()) throw NoSuchFileException(logConfig)
                    exec.addListener(CsvVariableWriter(logConfig, resultDir))
                } ?: run {
                    exec.addListener(CsvVariableWriter(null, resultDir))
                }

                relativeChartConfigPath?.also { configPath ->
                    val chartConfig = File(loader.ssdFile.parent, configPath)
                    if (!chartConfig.exists()) throw NoSuchFileException(chartConfig)
                    ChartLoader.load(chartConfig).forEach { chart ->
                        exec.addListener(chart)
                    }
                }

                val totalSimulationTime = stop - start
                val stepSize = (exec.algorithm as FixedStepMaster).baseStepSize
                val numSteps = (totalSimulationTime / stepSize).toLong()
                val aTenth = numSteps / 10

                val runner = ExecutionRunner(exec)

                targetRealtimeFactor?.also {
                    runner.targetRealTimeFactor = it
                } ?: run {
                    runner.enableRealTimeTarget.set(false)
                }
                runner.callback = {
                    val i = exec.stepNumber
                    if (i != 0L && i % aTenth == 0L || i == numSteps) {
                        val percentComplete = i / numSteps.toDouble() * 100
                        LOG.info(
                            "{}% complete, simulated {}s in {}s, target RTF={}, actual RTF={}",
                            percentComplete,
                            runner.simulationClock.formatForOutput(),
                            runner.wallClock.formatForOutput(),
                            if (runner.enableRealTimeTarget.get()) runner.targetRealTimeFactor else "unbounded",
                            runner.actualRealTimeFactor.formatForOutput()
                        )

                    }
                }
                runner.runUntil(stop).get()

            }*/

    }

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(SimulateSsp::class.java)

    }

}
