package no.ntnu.ihb.acco.render.jme

import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import no.ntnu.ihb.acco.core.AbstractEngineRunner
import no.ntnu.ihb.acco.core.Engine
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class JmeEngineRunner(
    engine: Engine
) : AbstractEngineRunner(engine) {

    private val app = JmeApp()
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

    private inner class JmeApp : SimpleApplication() {

        private val lock = ReentrantLock()
        private var initialized = lock.newCondition()

        private val root = Node()
        lateinit var renderSystem: JmeRenderSystem
            private set

        override fun start() {
            super.start()
            lock.withLock {
                initialized.await()
            }
        }

        override fun simpleInitApp() {
            super.setPauseOnLostFocus(false)
            super.flyCam.isDragToRotate = true
            super.rootNode.attachChild(root)
            super.flyCam.moveSpeed = 10f

            super.viewPort.backgroundColor.set(0.6f, 0.7f, 1f, 1f)

            super.stateManager.attach(object : AbstractAppState() {
                override fun cleanup() {
                    engine.close()
                }
            })

            setupLights()

            renderSystem = JmeRenderSystem(root, assetManager)

            lock.withLock {
                initialized.signalAll()
            }
        }

        override fun simpleUpdate(tpf: Float) {
            if (running) {
                stepEngine(tpf.toDouble())
            }
        }

        private fun setupLights() {

            DirectionalLight().apply {
                color = ColorRGBA.White.mult(0.8f)
                direction = Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal()
                rootNode.addLight(this)
            }

            DirectionalLight().apply {
                color = ColorRGBA.White.mult(0.8f)
                direction = Vector3f(0.5f, 0.5f, 0.5f).normalizeLocal()
                rootNode.addLight(this)
            }

            AmbientLight().apply {
                color = ColorRGBA.White.mult(0.3f)
                rootNode.addLight(this)
            }

        }

    }


}
