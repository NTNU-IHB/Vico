package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.vico.Vico
import no.ntnu.ihb.vico.cli.commands.Eval
import no.ntnu.ihb.vico.cli.commands.SimulateFmu
import no.ntnu.ihb.vico.cli.commands.SimulateSsp
import picocli.CommandLine

@CommandLine.Command(
    subcommands = [SimulateFmu::class, SimulateSsp::class, Eval::class]
)
class VicoCLI : Runnable {

    companion object {
        @JvmStatic
        fun main(args: Array<out String>) {
            CommandLine(VicoCLI()).execute(*args)
        }
    }

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["Display a help message"])
    private var helpRequested: Boolean = false

    @CommandLine.Option(names = ["-v", "--version"], description = ["Print the version of this application."])
    var showVersion = false

    override fun run() {
        if (showVersion) {
            println(Vico.version)
            return
        }
        CommandLine.usage(VicoCLI::class.java, System.out)
    }

}
