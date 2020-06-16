package no.ntnu.ihb.acco.render.jme

import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.GeometryComponentListener
import no.ntnu.ihb.acco.render.jme.objects.RenderNode
import org.joml.Quaterniond
import org.joml.Vector3d
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val MAX_QUEUE_SIZE = 50

class JmeRenderSystem : SimulationSystem(
    Family.all(TransformComponent::class.java, GeometryComponent::class.java).build()
) {

    private val app = JmeApp()

    private val tmpVec = Vector3d()
    private val tmpQuat = Quaterniond()

    private val map: MutableMap<Entity, RenderNode> = mutableMapOf()

    private val queue = ArrayDeque<JmeApp.() -> Unit>()

    init {
        priority = Int.MAX_VALUE
    }

    private fun invokeLater(task: JmeApp.() -> Unit) {
        queue.add(task)
        while (queue.size > MAX_QUEUE_SIZE) {
            queue.poll()
        }
    }

    override fun init(currentTime: Double) {
        app.start()
    }

    override fun entityAdded(entity: Entity) {

        val transform = entity.getComponent<TransformComponent>()
        val geometry = entity.getComponent<GeometryComponent>()

        val world = transform.getWorldMatrix()

        invokeLater {
            map.computeIfAbsent(entity) {

                geometry.createGeometry(app.assetManager).also { node ->

                    geometry.addListener(object : GeometryComponentListener {

                        override fun onColorChanged() {
                            node.setColor(geometry.getColor())
                        }

                        override fun onVisibilityChanged() {
                            node.setVisible(geometry.visible)
                        }

                        override fun onWireframeChanged() {
                            node.setWireframe(geometry.wireframe)
                        }
                    })

                    node.localTranslation.set(world.getTranslation(tmpVec))
                    node.localRotation.set(world.getNormalizedRotation(tmpQuat))
                    node.forceRefresh(true, true, true)

                    root.attachChild(node)
                }

            }
        }
    }

    override fun entityRemoved(entity: Entity) {
        invokeLater {
            root.detachChild(map.getValue(entity))
        }
    }

    override fun step(currentTime: Double, stepSize: Double) {

        entities.forEach { entity ->

            val node = map.getValue(entity)
            val transform = entity.getComponent<TransformComponent>()

            val world = transform.getWorldMatrix()

            invokeLater {
                node.localTranslation.set(world.getTranslation(tmpVec))
                node.localRotation.set(world.getNormalizedRotation(tmpQuat))
                node.forceRefresh(true, true, true)
            }

        }
    }

    private inner class JmeApp : SimpleApplication() {

        private val lock = ReentrantLock()
        private var initialized = lock.newCondition()

        val root = Node()

        override fun start() {
            super.start()
            lock.withLock {
                initialized.await()
            }
            Thread.sleep(500)
        }

        override fun simpleInitApp() {

            super.rootNode.attachChild(root)

            super.setPauseOnLostFocus(false)

            super.flyCam.isDragToRotate = true
            super.flyCam.moveSpeed = 10f

            super.viewPort.backgroundColor.set(0.6f, 0.7f, 1f, 1f)

            setupLights()
            emptyQueue()

            super.stateManager.attach(object : AbstractAppState() {
                override fun cleanup() {
                    engine.close()
                }
            })

            lock.withLock {
                initialized.signalAll()
            }
        }

        private fun emptyQueue() {
            while (!queue.isEmpty()) {
                queue.poll().invoke(this)
            }
        }

        override fun simpleUpdate(tpf: Float) {
            emptyQueue()
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
