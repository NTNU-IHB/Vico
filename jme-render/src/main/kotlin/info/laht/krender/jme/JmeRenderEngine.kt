package info.laht.krender.jme

import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.asset.AssetManager
import com.jme3.input.event.KeyInputEvent
import com.jme3.input.event.MouseButtonEvent
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.*
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import info.laht.krender.jme.extra.RawInputAdapter
import info.laht.krender.jme.proxy.*
import info.laht.krender.proxies.*
import no.ntnu.ihb.vico.render.AbstractRenderEngine
import no.ntnu.ihb.vico.render.mesh.TrimeshShape
import no.ntnu.ihb.vico.render.util.RenderContext
import org.joml.*
import org.joml.Matrix4f
import java.awt.event.KeyEvent
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal class JmeContext(
    val assetManager: AssetManager
) : RenderContext()

class JmeRenderEngine : AbstractRenderEngine() {

    private val renderer: JmeInternalRenderer
    private val parent: Node = Node("parent")

    private val ctx: JmeContext
        get() = renderer.ctx

    init {
        renderer = JmeInternalRenderer(parent).apply {
            start()
        }
    }

    override fun setBackGroundColor(color: Int) {
        ctx.invokeLater {
            renderer.viewPort.backgroundColor.set(ColorRGBA().fromIntARGB(color))
        }
    }

    override fun setCameraTransform(cameraTransform: Matrix4fc) {
        ctx.invokeLater {
            val pos = cameraTransform.getTranslation(org.joml.Vector3f())
            val rot = cameraTransform.getNormalizedRotation(Quaternionf())
            renderer.camera.location = Vector3f().set(pos)
            renderer.camera.rotation = Quaternion().set(rot)
        }
    }

    override fun close() {
        renderer.stop()
    }

    override fun createMesh(mesh: TrimeshShape): MeshProxy {
        return JmeMeshProxy(ctx, mesh).also {
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createMesh(source: File, scale: Float, offset: Matrix4fc?): MeshProxy {
        return JmeMeshProxy(ctx, source, scale, offset).also {
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createSphere(radius: Float, offset: Matrix4fc?): SphereProxy {
        return JmeSphereProxy(ctx, radius).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4fc?): BoxProxy {
        return JmeBoxProxy(ctx, width, height, depth).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createCylinder(radius: Float, height: Float, offset: Matrix4fc?): CylinderProxy {
        return JmeCylinderProxy(ctx, radius, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createCapsule(radius: Float, height: Float, offset: Matrix4fc?): CapsuleProxy {
        return JmeCapsuleProxy(ctx, radius, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createPlane(width: Float, height: Float, offset: Matrix4fc?): PlaneProxy {
        return JmePlaneProxy(ctx, width, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createAxis(size: Float): AxisProxy {
        return JmeAxisProxy(ctx, size).also {
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createHeightmap(
        width: Float,
        height: Float,
        widthSegments: Int,
        heightSegments: Int,
        heights: FloatArray
    ): HeightmapProxy {
        return JmeHeightmapProxy(ctx, width, height, widthSegments, heightSegments, heights).also {
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createCurve(radius: Float, points: List<Vector3fc>): CurveProxy {
        return JmeCurveProxy(ctx, radius, points).also {
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createArrow(length: Float): ArrowProxy {
        return JmeArrowProxy(ctx, length).also {
            ctx.invokeLater { parent.attachChild(it) }
        }
    }

    override fun createPointCloud(pointSize: Float, points: List<Vector3fc>): PointCloudProxy {
        TODO("Not yet implemented")
    }

    override fun createWater(width: Float, height: Float): WaterProxy {
        TODO("Not yet implemented")
    }

    override fun createLine(points: List<Vector3fc>): LineProxy {
        TODO("Not yet implemented")
    }

    private inner class JmeInternalRenderer(
        private val parent: Node
    ) : SimpleApplication() {

        private val lock = ReentrantLock()
        private var initialized = lock.newCondition()

        lateinit var ctx: JmeContext

        override fun start() {
            super.start()
            lock.withLock {
                initialized.await()
            }

        }

        override fun simpleInitApp() {

            super.setPauseOnLostFocus(false)

            super.flyCam.isDragToRotate = true
            super.flyCam.moveSpeed = 10f

            super.inputManager.addRawInputListener(InputListener())
            super.viewPort.backgroundColor.set(0.6f, 0.7f, 1f, 1f)

            super.stateManager.attach(object : AbstractAppState() {
                override fun cleanup() {
                    closeListener?.onClose()
                }
            })

            this.setupLights()
            this.rootNode.attachChild(parent)
            this.ctx = JmeContext(assetManager)

            lock.withLock {
                initialized.signalAll()
            }

        }

        override fun simpleUpdate(tpf: Float) {
            ctx.invokePendingTasks()
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
                mouseClickListener?.also {
                    val x = evt.x.toFloat()
                    val y = evt.y.toFloat()
                    val worldCoordinates = cam.getWorldCoordinates(Vector2f(x, y), 1000f)
                    val pos = Vector3d().set(cam.location)
                    val dir = Vector3d().set(Vector3f(cam.location).subtract(worldCoordinates).normalizeLocal())
                    val m = Matrix4f().rotateX(FastMath.PI / 2)
                    pos.mulPosition(m)
                    dir.mulDirection(m)
                    it.onMouseClicked(pos, dir)
                }
            }

            override fun onKeyEvent(evt: KeyInputEvent) {
                val c = evt.keyChar.toInt()
                if (c != 0) {
                    val keyCode = KeyEvent.getExtendedKeyCodeForChar(c)
                    keyListener?.onKeyPressed(keyCode)
                }
            }

        }

    }

}
