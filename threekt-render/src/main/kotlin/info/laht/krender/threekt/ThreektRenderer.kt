package info.laht.krender.threekt

import info.laht.krender.proxies.*
import info.laht.threekt.Window
import info.laht.threekt.WindowClosingCallback
import info.laht.threekt.cameras.PerspectiveCamera
import info.laht.threekt.controls.OrbitControls
import info.laht.threekt.core.Clock
import info.laht.threekt.input.KeyAction
import info.laht.threekt.math.Color
import info.laht.threekt.math.Matrix4
import info.laht.threekt.renderers.GLRenderer
import info.laht.threekt.scenes.Scene
import no.ntnu.ihb.vico.render.AbstractRenderEngine
import no.ntnu.ihb.vico.render.mesh.TrimeshShape
import no.ntnu.ihb.vico.render.util.RenderContext
import org.joml.Matrix4fc
import org.joml.Vector3fc
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

class ThreektRenderer : AbstractRenderEngine() {

    private val ctx: RenderContext = RenderContext()

    private val scene: Scene = Scene().apply {
        setBackground(Color.aliceblue)
    }
    private val internalRenderer: InternalRenderer

    init {
        internalRenderer = InternalRenderer().apply {
            start()
        }
    }

    override fun setCameraTransform(cameraTransform: Matrix4fc) {
        val m = Matrix4().set(cameraTransform)
        ctx.invokeLater {
            internalRenderer.apply {
                camera.position.setFromMatrixPosition(m)
                camera.quaternion.setFromRotationMatrix(m)
                controls.update()
            }
        }
    }

    override fun setBackGroundColor(color: Int) {
        scene.setBackground(color)
    }

    override fun createAxis(size: Float): AxisProxy {
        return ThreektAxisProxy(ctx, size).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createArrow(length: Float): ArrowProxy {
        return ThreektArrowProxy(ctx, length).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createMesh(mesh: TrimeshShape): MeshProxy {
        return ThreektTrimeshProxy(ctx, mesh).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createMesh(source: File, scale: Float, offset: Matrix4fc?): MeshProxy {
        return ThreektTrimeshProxy(ctx, source, scale).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createSphere(radius: Float, offset: Matrix4fc?): SphereProxy {
        return ThreektSphereProxy(ctx, radius).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createPlane(width: Float, height: Float, offset: Matrix4fc?): PlaneProxy {
        return ThreektPlaneProxy(ctx, width, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4fc?): BoxProxy {
        return ThreektBoxProxy(ctx, width, height, depth).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createCylinder(radius: Float, height: Float, offset: Matrix4fc?): CylinderProxy {
        return ThreektCylinderProxy(ctx, radius, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createCapsule(radius: Float, height: Float, offset: Matrix4fc?): CapsuleProxy {
        TODO("Not yet implemented")
    }

    override fun createCurve(radius: Float, points: List<Vector3fc>): CurveProxy {
        return ThreektCurveProxy(ctx, radius, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createLine(points: List<Vector3fc>): LineProxy {
        return ThreektLineProxy(ctx, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createHeightmap(
        width: Float,
        height: Float,
        widthSegments: Int,
        heightSegments: Int,
        heights: FloatArray
    ): HeightmapProxy {
        return ThreektHeightmapProxy(ctx, width, height, widthSegments, heightSegments, heights).also {
            ctx.invokeLater {
                scene.attach(it.parentNode)
            }
        }
    }

    override fun createWater(width: Float, height: Float): WaterProxy {
        return ThreektWaterProxy(ctx, width, height).also {
            internalRenderer.water = it
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createPointCloud(pointSize: Float, points: List<Vector3fc>): PointCloudProxy {
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
            window?.close()
        }

        fun run() {

            window = Window(
                antialias = 4,
                resizeable = true
            )

            window?.use { window ->

                lock.withLock {
                    initialized.signalAll()
                }

                window.onCloseCallback = WindowClosingCallback {
                    closeListener?.onClose()
                }

                val renderer = GLRenderer(window.size)
                camera = PerspectiveCamera(75, window.aspect, 0.1, 1000).apply {
                    position.set(0f, 0f, 5f)
                }
                controls = OrbitControls(camera, window)

                window.onWindowResize { width, height ->
                    camera.aspect = window.aspect
                    camera.updateProjectionMatrix()
                    renderer.setSize(width, height)
                }

                window.addKeyListener {
                    if (it.action == KeyAction.PRESS) {
                        keyListener?.onKeyPressed(it.keyCode)
                    }
                }

                val clock = Clock()
                window.animate {
                    ctx.invokePendingTasks()

                    water?.water?.apply {
                        val wTime = uniforms["time"]!!.value as Float
                        uniforms["time"]!!.value = wTime + (0.2f * clock.getDelta())
                    }

                    renderer.render(scene, camera)
                }

            }

        }

    }

}
