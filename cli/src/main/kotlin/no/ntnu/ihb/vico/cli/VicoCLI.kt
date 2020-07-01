package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.vico.cli.commands.Run
import no.ntnu.ihb.vico.cli.commands.SimulateFmu
import no.ntnu.ihb.vico.cli.commands.SimulateSsp
import picocli.CommandLine

fun cliExec(f: () -> Array<String>) {
    VicoCLI.main(f.invoke())
}

fun Array<String>.cliExec() = cliExec { this }

@CommandLine.Command(
    subcommands = [Run::class, SimulateFmu::class, SimulateSsp::class]
)
class VicoCLI : Runnable {

    companion object {
        @JvmStatic
        fun main(args: Array<out String>) {
            CommandLine(VicoCLI()).execute(*args)
        }
    }

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display a help message"])
    private var helpRequested: Boolean = false

    override fun run() {
        CommandLine.usage(VicoCLI::class.java, System.out)
    }

}
