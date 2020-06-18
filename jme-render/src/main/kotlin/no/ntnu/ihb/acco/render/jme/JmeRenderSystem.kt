package no.ntnu.ihb.acco.render.jme

import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.input.KeyInput
import com.jme3.input.event.KeyInputEvent
import com.jme3.input.event.MouseButtonEvent
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.FastMath
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import no.ntnu.ihb.acco.input.ClickListener
import no.ntnu.ihb.acco.input.KeyListener
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.objects.RawInputAdapter
import no.ntnu.ihb.acco.render.jme.objects.RenderNode
import org.joml.Matrix4f
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

        val geometry = entity.getComponent<GeometryComponent>()

        invokeLater {
            map.computeIfAbsent(entity) {

                geometry.createGeometry(app.assetManager).also { node ->

                    geometry.addEventListener("onVisibilityChanged") {
                        node.setVisible(it.target())
                    }

                    geometry.addEventListener("onWireframeChanged") {
                        node.setWireframe(it.target())
                    }

                    geometry.addEventListener("onColorChanged") {
                        node.setColor(it.target())
                    }

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

    override fun postInit() {
        updateTransforms()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        updateTransforms()
    }

    fun updateTransforms() {
        for (entity in entities) {

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

        var keyListener: KeyListener? = null
        var clickListener: ClickListener? = null

        val root = Node()

        override fun start() {
            super.start()
            lock.withLock {
                initialized.await()
            }
        }

        override fun simpleInitApp() {

            super.rootNode.attachChild(root)

            super.setPauseOnLostFocus(false)

            super.flyCam.isDragToRotate = true
            super.flyCam.moveSpeed = 10f

            super.inputManager.addRawInputListener(InputListener())
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

        private inner class InputListener : RawInputAdapter() {

            override fun onMouseButtonEvent(evt: MouseButtonEvent) {
                clickListener?.also {
                    val x = evt.x.toFloat()
                    val y = evt.y.toFloat()
                    val worldCoordinates = cam.getWorldCoordinates(Vector2f(x, y), 1000f)
                    val pos = Vector3d().set(cam.location)
                    val dir = Vector3d().set(Vector3f(cam.location).subtract(worldCoordinates).normalizeLocal())
                    val m = Matrix4f().rotateX(FastMath.PI / 2)
                    pos.mulPosition(m)
                    dir.mulDirection(m)
                    it.onMousePressed(pos, dir)
                }
            }

            override fun onKeyEvent(evt: KeyInputEvent) {
                if (evt.isPressed) {
                    val keyCode = evt.keyCode
                    keyListener?.also {
                        it.onKeyPressed(keyCode.toKeyStoke())
                    }
                    when (keyCode) {
                        KeyInput.KEY_F1 -> {
                            //showCollisionGeometries = !showCollisionGeometries
                        }
                        KeyInput.KEY_E -> {
                            engine.runner.paused.set(!engine.runner.paused.get())
                        }
                    }
                }
            }

        }

    }

}
