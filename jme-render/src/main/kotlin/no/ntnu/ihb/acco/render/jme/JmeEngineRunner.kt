package no.ntnu.ihb.acco.render.jme

import no.ntnu.ihb.acco.core.AbstractEngineRunner
import no.ntnu.ihb.acco.core.Engine

class JmeEngineRunner(
    engine: Engine
) : AbstractEngineRunner(engine) {

    private val app = JmeApp(engine) { tpf ->
        if (running) {
            stepEngine(tpf.toDouble())
        }
    }
    private var running = false

    init {
        app.start()
        engine.addSystem(app.renderSystem)
    }

    override fun start() {
        running = true
    }

    fun pause(flag: Boolean) {
        running = flag
    }

    override fun stop() {
        app.stop()
    }

}
