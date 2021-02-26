package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.render.listeners.CloseListener
import no.ntnu.ihb.vico.render.listeners.KeyListener
import no.ntnu.ihb.vico.render.listeners.MouseClickListener
import no.ntnu.ihb.vico.render.mesh.TrimeshShape
import no.ntnu.ihb.vico.render.proxies.*
import org.joml.Matrix4fc
import org.joml.Vector3fc
import java.io.Closeable
import java.io.File

interface RenderEngine : Closeable {

    fun show()

    fun setBackGroundColor(color: Int)
    fun setCameraTransform(cameraTransform: Matrix4fc)

    fun createAxis(size: Float): AxisProxy
    fun createArrow(length: Float): ArrowProxy

    fun createMesh(mesh: TrimeshShape): MeshProxy
    fun createMesh(source: File, scale: Float = 1f, offset: Matrix4fc? = null): MeshProxy

    fun createSphere(radius: Float, offset: Matrix4fc? = null): SphereProxy
    fun createPlane(width: Float, height: Float, offset: Matrix4fc? = null): PlaneProxy
    fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4fc? = null): BoxProxy
    fun createCylinder(radius: Float, height: Float, offset: Matrix4fc? = null): CylinderProxy
    fun createCapsule(radius: Float, height: Float, offset: Matrix4fc? = null): CapsuleProxy

    fun createLine(points: List<Vector3fc>): LineProxy
    fun createCurve(radius: Float, points: List<Vector3fc>): CurveProxy
    fun createHeightmap(
        width: Float,
        height: Float,
        widthSegments: Int,
        heightSegments: Int,
        heights: FloatArray
    ): HeightmapProxy

    fun createWater(width: Float, height: Float): WaterProxy
    fun createPointCloud(pointSize: Float, points: List<Vector3fc>): PointCloudProxy

    fun registerKeyListener(listener: KeyListener)
    fun registerClickListener(listener: MouseClickListener)

    fun registerCloseListener(listener: CloseListener)

}

abstract class AbstractRenderEngine : RenderEngine {

    protected var keyListener: KeyListener? = null
    protected var closeListener: CloseListener? = null
    protected var mouseClickListener: MouseClickListener? = null

    override fun registerKeyListener(listener: KeyListener) {
        this.keyListener = listener
    }

    override fun registerCloseListener(listener: CloseListener) {
        this.closeListener = listener
    }

    override fun registerClickListener(listener: MouseClickListener) {
        this.mouseClickListener = listener
    }

}

