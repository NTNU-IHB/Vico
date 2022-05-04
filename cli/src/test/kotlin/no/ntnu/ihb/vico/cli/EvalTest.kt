package no.ntnu.ihb.vico.cli

import org.junit.jupiter.api.Test
import java.io.File

class EvalTest {

    @Test
    fun testEval() {

        val cl = EvalTest::class.java.classLoader
        val file = File(cl.getResource("script.main.kts")!!.file.replace("%20", " "))

        VicoCLI.main(arrayOf("eval", file.absolutePath))

    }

}
