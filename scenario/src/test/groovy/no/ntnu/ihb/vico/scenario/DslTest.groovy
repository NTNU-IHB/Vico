package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.acco.core.Engine
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DslTest {

    @Test
    @Disabled
    void testDsl() {

        File dsl = new File(DslTest.class.classLoader.getResource("action1.groovy").file)

        Engine engine = new Engine()
        new ScenarioDsl(engine).parse(dsl)

        engine.stepUntil(5.0)

    }
}
