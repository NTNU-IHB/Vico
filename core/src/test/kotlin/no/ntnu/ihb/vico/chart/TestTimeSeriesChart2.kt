package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.vico.TestSsp
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.ssp.SSPLoader
import java.io.File

internal object TestTimeSeriesChart2 {

    private fun getTestResource(name: String): File {
        return File(TestTimeSeriesChart2::class.java.classLoader.getResource(name)!!.file)
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val sspFile = TestSsp.get("ControlledDrivetrain.ssp")

        Engine(1.0 / 1000).use { engine ->

            SSPLoader(sspFile).load().apply(engine, FixedStepMaster(false))

            val config = getTestResource("chartconfig/ChartConfig2.xml")
            ChartLoader.load(config).forEach {
                engine.addSystem(it)
            }

            engine.stepUntil(10.0)

        }

    }

}
