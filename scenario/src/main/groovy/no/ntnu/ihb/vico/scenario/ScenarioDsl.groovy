package no.ntnu.ihb.vico.scenario

import groovy.transform.CompileStatic
import no.ntnu.ihb.acco.core.Engine

class ScenarioDsl {

    Engine engine

    ScenarioDsl(Engine engine) {
        this.engine = engine
    }

    ScenarioDelegate parse(File dsl) {

        Binding bindings = new Binding()
        bindings.setProperty("engine", engine)
        Script dslScript = new GroovyShell(bindings).parse(dsl.text)
        def scenario = new ScenarioDelegate(engine)

        dslScript.metaClass = createEMC(dslScript.class, {
            ExpandoMetaClass emc ->
                emc.scenario = {
                    Closure cl ->
                        cl.delegate = scenario
                        cl.resolveStrategy = DELEGATE_ONLY
                        cl()
                }
        })
        dslScript.run()

        return scenario
    }

    private static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }

}

@CompileStatic
class ScenarioDelegate {

    Engine engine

    ScenarioDelegate(Engine engine) {
        this.engine = engine
    }

    void task() {

    }

}
