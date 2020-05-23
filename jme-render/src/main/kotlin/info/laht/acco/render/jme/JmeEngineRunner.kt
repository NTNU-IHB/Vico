package info.laht.acco.render.jme

import info.laht.acco.core.AbstractEngineRunner
import info.laht.acco.core.Engine

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
