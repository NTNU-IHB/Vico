package info.laht.krender.threekt

import info.laht.threekt.Window
import info.laht.threekt.WindowClosingCallback
import info.laht.threekt.cameras.PerspectiveCamera
import info.laht.threekt.controls.OrbitControls
import info.laht.threekt.core.Clock
import info.laht.threekt.input.KeyAdapter
import info.laht.threekt.input.KeyEvent
import info.laht.threekt.lights.AmbientLight
import info.laht.threekt.math.Color
import info.laht.threekt.math.Matrix4
import info.laht.threekt.renderers.GLRenderer
import info.laht.threekt.scenes.Scene
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.ObserverSystem
import no.ntnu.ihb.vico.render.Camera
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.Trail
import no.ntnu.ihb.vico.render.Water
import no.ntnu.ihb.vico.render.mesh.*
import no.ntnu.ihb.vico.render.util.RenderContext
import org.joml.Matrix4fc
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3fc
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

class ThreektRenderer : ObserverSystem(Family.all) {

    private val ctx: RenderContext = RenderContext()
    private val proxies = mutableMapOf<Entity, ThreektProxy>()
    private val trailProxies = mutableMapOf<Entity, Pair<ThreektLineProxy, MutableList<Vector3f>>>()

    private val scene: Scene = Scene().apply {
        setBackground(Color.aliceblue)
    }

    private val internalRenderer = InternalRenderer()

    init {
        priority = Int.MAX_VALUE
        internalRenderer.start()
    }

    override fun postInit() {
        updateTransforms()
        updateTrails()
    }

    override fun observe(currentTime: Double) {
        postInit()
    }

    private fun updateTransforms() {
        proxies.forEach { (e, p) ->
            val t = e.get<Transform>()
            val m = t.getWorldMatrixf()
            p.setTransform(m)
        }
    }

    private fun updateTrails() {
        trailProxies.forEach { (e, p) ->

            val t = e.get<Transform>()
            val v = Vector3f().set(t.getTranslation(Vector3d()))

            val points = p.second.apply {
                add(v)
            }
            //TODO compute line length

            if (points.size > 100) {
                points.removeAt(0)
            }

            p.first.update(points)
        }
    }

    override fun entityAdded(entity: Entity) {

        val t = entity.getOrNull<Transform>()

        entity.getOrNull<Geometry>()?.also { g ->
            val proxy = when (val shape = g.shape) {
                is SphereShape -> {
                    createSphere(shape.radius, g.offset)
                }
                is PlaneShape -> {
                    createPlane(shape.width, shape.height, g.offset)
                }
                is BoxShape -> {
                    createBox(shape.width, shape.depth, shape.height, g.offset)
                }
                is CylinderShape -> {
                    createCylinder(shape.radius, shape.height, g.offset)
                }
                is TrimeshShape -> {
                    createMesh(shape)
                }
                else -> null
            }
            proxy?.also { p ->

                p.setOpacity(g.opacity)
                p.setColor(g.color)
                p.setWireframe(g.wireframe)

                g.addEventListener("onVisibilityChanged") {
                    p.setVisible(it.value())
                }
                g.addEventListener("onWireframeChanged") {
                    p.setWireframe(it.value())
                }
                g.addEventListener("onColorChanged") {
                    p.setColor(it.value())
                }
                g.addEventListener("onOpacityChanged") {
                    p.setOpacity(it.value())
                }

                proxies[entity] = p
            }
        }
        entity.getOrNull<Water>()?.also { w ->
            internalRenderer.water = createWater(w.width, w.height)
        }
        entity.getOrNull<Trail>()?.also { trail ->
            val proxy = createLine(emptyList()).apply {
                setColor(trail.color)
            }
            trailProxies[entity] = proxy to mutableListOf()
        }
        entity.getOrNull<Camera>()?.also { c ->
            if (t != null) {
                val p = t.getTranslation()
                ctx.invokeLater {
                    internalRenderer.camera.fov = c.fov.toFloat()
                    internalRenderer.camera.position.set(
                        p.x.toFloat(), p.y.toFloat(), p.z.toFloat()
                    )
                }
            }
        }
    }

    override fun entityRemoved(entity: Entity) {
        proxies.remove(entity)?.dispose()
    }


    fun setCameraTransform(cameraTransform: Matrix4fc) {
        val m = Matrix4().set(cameraTransform)
        ctx.invokeLater {
            internalRenderer.apply {
                camera.position.setFromMatrixPosition(m)
                camera.quaternion.setFromRotationMatrix(m)
                controls.update()
            }
        }
    }

    fun setBackGroundColor(color: Int) {
        scene.setBackground(color)
    }

    fun createAxis(size: Float): ThreektAxisProxy {
        return ThreektAxisProxy(ctx, size).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createArrow(length: Float): ThreektArrowProxy {
        return ThreektArrowProxy(ctx, length).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createMesh(mesh: TrimeshShape): ThreektTrimeshProxy {
        return ThreektTrimeshProxy(ctx, mesh).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createMesh(source: File, scale: Float): ThreektTrimeshProxy {
        return ThreektTrimeshProxy(ctx, source, scale).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createSphere(radius: Float, offset: Matrix4fc? = null): ThreektSphereProxy {
        return ThreektSphereProxy(ctx, radius).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createPlane(width: Float, height: Float, offset: Matrix4fc? = null): ThreektPlaneProxy {
        return ThreektPlaneProxy(ctx, width, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4fc? = null): ThreektBoxProxy {
        return ThreektBoxProxy(ctx, width, height, depth).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createCylinder(radius: Float, height: Float, offset: Matrix4fc? = null): ThreektCylinderProxy {
        return ThreektCylinderProxy(ctx, radius, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createCurve(radius: Float, points: List<Vector3fc>): ThreektCurveProxy {
        return ThreektCurveProxy(ctx, radius, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createLine(points: List<Vector3fc>): ThreektLineProxy {
        return ThreektLineProxy(ctx, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createHeightmap(
        width: Float,
        height: Float,
        widthSegments: Int,
        heightSegments: Int,
        heights: FloatArray
    ): ThreektHeightmapProxy {
        return ThreektHeightmapProxy(ctx, width, height, widthSegments, heightSegments, heights).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createWater(width: Float, height: Float): ThreektWaterProxy {
        return ThreektWaterProxy(ctx, width, height).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    fun createPointCloud(pointSize: Float, points: List<Vector3fc>): ThreektPointCloudProxy {
        return ThreektPointCloudProxy(ctx, pointSize, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun close() {
        internalRenderer.close()
    }

    private inner class InternalRenderer {

        private val lock = ReentrantLock()
        private var initialized = lock.newCondition()

        private var window: Window? = null
        lateinit var camera: PerspectiveCamera
        lateinit var controls: OrbitControls

        var water: ThreektWaterProxy? = null

        fun start() {

            thread(start = true) {
                run()
            }

            lock.withLock {
                initialized.await()
            }
        }

        fun close() {
            try {
                window?.close()
            } catch (ex: InterruptedException) {
                // ignore
            }
        }

        fun run() {

            Window(
                title = "Vico",
                antialias = 4,
                resizeable = true
            ).use { window ->

                this.window = window

                lock.withLock {
                    initialized.signalAll()
                }

                window.onCloseCallback = WindowClosingCallback {
                    engine.close()
                }

                val renderer = GLRenderer(window.size)
                camera = PerspectiveCamera(75, window.aspect, 0.1, 10000).apply {
                    position.set(15f, 15f, 15f)
                }
                controls = OrbitControls(camera, window)

                scene.add(AmbientLight())

                window.onWindowResize { width, height ->
                    camera.aspect = window.aspect
                    camera.updateProjectionMatrix()
                    renderer.setSize(width, height)
                }

                window.addKeyListener (object: KeyAdapter(){
                    override fun onKeyPressed(event: KeyEvent) {
                        val key = when (event.keyCode) {
                            65 -> "a"
                            69 -> "e"
                            68 -> "d"
                            87 -> "w"
                            83 -> "s"
                            else -> null
                        }
                        key?.also { engine.registerKeyPress(it) }
                    }
                })

                val clock = Clock()
                window.animate {

                    water?.water?.apply {
                        val wTime = uniforms["time"]!!.value as Float
                        uniforms["time"]!!.value = wTime + (0.2f * clock.getDelta())
                    }

                    renderer.render(scene, camera)

                    ctx.invokePendingTasks() // invoke after render to avoid crash related to dispose

                }

            }

        }

    }

}
