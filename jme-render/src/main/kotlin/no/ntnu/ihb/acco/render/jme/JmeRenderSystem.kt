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
import no.ntnu.ihb.acco.input.KeyStroke
import no.ntnu.ihb.acco.render.Camera
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.objects.RawInputAdapter
import no.ntnu.ihb.acco.render.jme.objects.RenderNode
import org.joml.Matrix4f
import org.joml.Quaterniond
import org.joml.Vector3d
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val MAX_TRANSFORM_QUEUE_SIZE = 1000

class JmeRenderSystem : SimulationSystem(
    Family.all(TransformComponent::class.java).one(GeometryComponent::class.java, Camera::class.java).build()
) {

    private val app = JmeApp()
    private val queueMutex = Unit

    private val tmpVec = Vector3d()
    private val tmpQuat = Quaterniond()

    private val map: MutableMap<Entity, RenderNode> = mutableMapOf()

    private val queue = ArrayDeque<JmeApp.() -> Unit>()
    private val transformQueue = ArrayDeque<JmeApp.() -> Unit>()

    private var cameraEntity: Entity? = null

    init {
        priority = Int.MAX_VALUE
    }

    private fun invokeLater(task: JmeApp.() -> Unit) {
        synchronized(queueMutex) {
            queue.add(task)
        }
    }

    private fun transformContext(task: JmeApp.() -> Unit) {
        synchronized(queueMutex) {
            transformQueue.add(task)
            while (transformQueue.size > MAX_TRANSFORM_QUEUE_SIZE) {
                transformQueue.poll()
            }
        }
    }

    override fun init(currentTime: Double) {
        app.start()
    }

    override fun entityAdded(entity: Entity) {

        entity.getComponentOrNull<Camera>()?.also {
            cameraEntity = entity
        }

        entity.getComponentOrNull<GeometryComponent>()?.also { geometry ->
            invokeLater {
                val node = map.computeIfAbsent(entity) {

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

                    }

                }
                root.attachChild(node)
            }
        }

    }

    override fun entityRemoved(entity: Entity) {
        invokeLater {
            map[entity]?.also { root.detachChild(it) }
        }
    }

    override fun postInit() {
        updateTransforms()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        updateTransforms()
    }

    private fun updateTransform(node: Node, transform: TransformComponent) {
        val world = transform.frame.getWorldMatrix()
        transformContext {
            node.localTranslation.set(world.getTranslation(tmpVec))
            node.localRotation.set(world.getNormalizedRotation(tmpQuat))
            node.forceRefresh(true, true, true)
        }
    }

    private fun updateTransforms() {
        for (entity in entities) {
            map[entity]?.also { node ->
                updateTransform(node, entity.getComponent())
            }
        }
    }

    override fun close() {
        app.stop()
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
            synchronized(queueMutex) {
                while (!queue.isEmpty()) {
                    queue.poll().invoke(this)
                }
                while (!transformQueue.isEmpty()) {
                    transformQueue.poll().invoke(this)
                }
            }
        }

        override fun simpleUpdate(tpf: Float) {
            emptyQueue()
            updateCamera()
        }

        private fun updateCamera() {
            cameraEntity?.also {
                flyCam.isEnabled = false
                val world = it.getComponent<TransformComponent>().frame.getWorldMatrix()
                invokeLater {
                    cam.location.set(world.getTranslation(tmpVec))
                    cam.rotation.set(world.getNormalizedRotation(tmpQuat))
                    cam.update()
                }
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
                        KeyInput.KEY_E -> engine.registerKeyPress(KeyStroke.KEY_E)
                        KeyInput.KEY_R -> engine.registerKeyPress(KeyStroke.KEY_R)
                        KeyInput.KEY_W -> engine.registerKeyPress(KeyStroke.KEY_W)
                        KeyInput.KEY_A -> engine.registerKeyPress(KeyStroke.KEY_A)
                        KeyInput.KEY_S -> engine.registerKeyPress(KeyStroke.KEY_S)
                        KeyInput.KEY_D -> engine.registerKeyPress(KeyStroke.KEY_D)
                    }
                }
            }

        }

    }

}
