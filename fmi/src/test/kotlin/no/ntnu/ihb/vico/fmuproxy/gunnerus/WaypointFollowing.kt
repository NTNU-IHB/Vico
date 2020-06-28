package no.ntnu.ihb.vico.fmuproxy.gunnerus

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.vico.ssp.SSPLoader
import java.io.File

internal object WaypointFollowing {

    @JvmStatic
    fun main(args: Array<String>) {

        val sspDir = File("E:\\dev\\gunnerus_case\\vico-playground\\data\\ssp\\trajectory")

        Engine(1.0 / 20).use { engine ->

            val structure = SSPLoader(sspDir).load()
            structure.apply(engine, "initialValues")

            engine.stepUntil(100.0)

        }

    }

}
