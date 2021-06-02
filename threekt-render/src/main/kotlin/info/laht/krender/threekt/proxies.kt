package info.laht.krender.threekt

import info.laht.threekt.Colors
import info.laht.threekt.Side
import info.laht.threekt.TextureWrapping
import info.laht.threekt.core.*
import info.laht.threekt.extras.objects.Water
import info.laht.threekt.geometries.*
import info.laht.threekt.helpers.ArrowHelper
import info.laht.threekt.helpers.AxesHelper
import info.laht.threekt.loaders.OBJLoader
import info.laht.threekt.loaders.STLLoader
import info.laht.threekt.loaders.TextureLoader
import info.laht.threekt.loaders.load
import info.laht.threekt.materials.MaterialWithColor
import info.laht.threekt.materials.MaterialWithWireframe
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.materials.PointsMaterial
import info.laht.threekt.math.Color
import info.laht.threekt.math.Vector3
import info.laht.threekt.math.curves.CatmullRomCurve3
import info.laht.threekt.objects.Line
import info.laht.threekt.objects.Mesh
import info.laht.threekt.objects.Points
import no.ntnu.ihb.vico.render.mesh.TrimeshShape
import no.ntnu.ihb.vico.render.mesh.TrimeshShapeWithSource
import no.ntnu.ihb.vico.render.util.ExternalSource
import no.ntnu.ihb.vico.render.util.FileSource
import no.ntnu.ihb.vico.render.util.RenderContext
import org.joml.Matrix4fc
import org.joml.Quaternionfc
import org.joml.Vector3fc
import java.io.File
import java.util.*
import kotlin.math.PI

open class ThreektProxy(
    val ctx: RenderContext
) {

    val parentNode = Object3DImpl().apply { matrixAutoUpdate = false }
    private val childNode = Object3DImpl().apply { matrixAutoUpdate = false }

    init {
        ctx.invokeLater {
            parentNode.add(childNode)
        }
    }

    fun setTranslate(v: Vector3fc) {
        ctx.invokeLater {
            parentNode.position.set(v)
            parentNode.updateMatrix()
        }
    }

    fun setRotate(q: Quaternionfc) {
        ctx.invokeLater {
            parentNode.quaternion.set(q)
            parentNode.updateMatrix()
        }
    }

    fun setTransform(m: Matrix4fc) {
        ctx.invokeLater {
            parentNode.matrix.set(m)
        }
    }

    fun setOffsetTransform(offset: Matrix4fc) {
        ctx.invokeLater {
            childNode.matrix.set(offset)
        }
    }

    fun setTexture(source: ExternalSource) {
        if (source is FileSource) {
            ctx.invokeLater {
                for (o in childNode.children) {
                    if (o is Mesh) {
                        val m = o.material
                        if (m is MeshBasicMaterial) {
                            m.map = TextureLoader.load(source.file.absolutePath)
                        }
                    }
                }
            }
        }
    }

    fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is Mesh) {
                    val material = o.material
                    if (material is MaterialWithWireframe) {
                        material.wireframe = flag
                    }
                }
            }
        }
    }

    fun setColor(color: Int) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is MaterialObject) {
                    val material = o.material
                    if (material is MaterialWithColor) {
                        material.color.set(color)
                    }
                }
            }
        }
    }

    fun setOpacity(value: Float) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is MaterialObject) {
                    val material = o.material
                    material.opacity = value
                    material.transparent = true
                }
            }
        }
    }

    fun setVisible(visible: Boolean) {
        ctx.invokeLater {
            childNode.visible = visible
        }
    }

    fun dispose() {
        ctx.invokeLater {
            childNode.parent?.remove(childNode)
        }
    }

    protected fun attachChild(obj: Object3D) {
        childNode.add(obj)
    }

}

class ThreektPlaneProxy(
    ctx: RenderContext,
    width: Float,
    height: Float
) : ThreektProxy(ctx) {

    private val geometry = PlaneBufferGeometry(width, height)
    private val mesh = Mesh(geometry)

    init {

        mesh.material.side = Side.Double

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

class ThreektBoxProxy(
    ctx: RenderContext,
    width: Float,
    height: Float,
    depth: Float
) : ThreektProxy(ctx) {

    private val geometry = BoxBufferGeometry(width, height, depth)
    private val mesh = Mesh(geometry)

    init {

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

class ThreektSphereProxy(
    ctx: RenderContext,
    private var radius: Float
) : ThreektProxy(ctx) {

    private val geometry = SphereBufferGeometry(radius)
    private val mesh = Mesh(geometry)

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    fun setRadius(radius: Float) {
        val scale = radius / this.radius
        geometry.scale(scale)
        this.radius = radius
    }

}

class ThreektCylinderProxy(
    ctx: RenderContext,
    private var radius: Float,
    private var height: Float
) : ThreektProxy(ctx) {

    private val geometry = CylinderBufferGeometry(radius, height)
    private val mesh = Mesh(geometry)

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    fun setRadius(radius: Float) {
        val scale = radius / this.radius
        geometry.scale(scale)
        this.radius = radius
    }

    fun setHeight(height: Float) {
        val scale = (height / this.height)
        geometry.scale(1f, scale, 1f)
        this.height = height
    }

}

class ThreektTrimeshProxy private constructor(
    ctx: RenderContext
) : ThreektProxy(ctx) {

    constructor(ctx: RenderContext, trimesh: TrimeshShape) : this(ctx) {
        if (trimesh is TrimeshShapeWithSource && trimesh.source != null) {
            loadFromFile(trimesh.source!!, 1f)
        } else {
            val geometry = BufferGeometry().apply {
                setIndex(IntBufferAttribute(trimesh.indices.toIntArray(), 1))
                addAttribute("position", FloatBufferAttribute(trimesh.vertices.toFloatArray(), 3))
                addAttribute("normal", FloatBufferAttribute(trimesh.normals.toFloatArray(), 3))
                addAttribute("uv", FloatBufferAttribute(trimesh.uvs.toFloatArray(), 2))
            }
            geometry.computeBoundingSphere()

            val mesh = Mesh(geometry).apply {
                material.side = Side.Double
            }
            ctx.invokeLater {
                attachChild(mesh)
            }
        }
    }

    constructor(ctx: RenderContext, source: File, scale: Float) : this(ctx) {
        loadFromFile(source, scale)
    }

    private fun loadFromFile(source: File, scale: Float) {
        val mesh = when (val ext = source.extension.lowercase(Locale.getDefault())) {
            "obj" -> OBJLoader().load(source.absolutePath)
            "stl" -> Mesh(STLLoader().load(source.absolutePath))
            else -> throw UnsupportedOperationException("Unsupported extension: $ext")
        }
        mesh.scale.set(scale, scale, scale)
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

}

class ThreektPointCloudProxy(
    ctx: RenderContext,
    pointSize: Float,
    points: List<Vector3fc>,
) : ThreektProxy(ctx) {

    init {

        var i = 0
        val positions = FloatArray(3 * points.size)
        points.forEach { v ->
            positions[i++] = v.x()
            positions[i++] = v.y()
            positions[i++] = v.z()
        }

        val geometry = BufferGeometry()
        geometry.addAttribute("position", FloatBufferAttribute(positions, 3))

        val material = PointsMaterial().apply {
            size = pointSize
            vertexColors = Colors.Vertex
        }

        ctx.invokeLater {
            attachChild(Points(geometry, material))
        }

    }

}

class ThreektCurveProxy(
    ctx: RenderContext,
    private val radius: Float,
    points: List<Vector3fc>
) : ThreektProxy(ctx) {

    private val mesh = Mesh(
        TubeBufferGeometry(
            CatmullRomCurve3(points.map { Vector3().set(it) }),
            radius = radius
        )
    ).apply {
        material.side = Side.Double
        (material as MaterialWithColor).color.set(Color.gray)
    }

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    fun update(points: List<Vector3fc>) {
        val curve = CatmullRomCurve3(points.map { Vector3().set(it) })
        ctx.invokeLater {
            mesh.geometry.dispose()
            mesh.geometry = TubeBufferGeometry(curve, radius = radius)
        }
    }
}

class ThreektLineProxy(
    ctx: RenderContext,
    points: List<Vector3fc>
) : ThreektProxy(ctx) {

    private val line = Line(createGeometry(points))

    init {
        ctx.invokeLater {
            attachChild(line)
        }
    }

    fun update(points: List<Vector3fc>) {
        ctx.invokeLater {
            line.geometry.dispose()
            line.geometry = createGeometry(points)
        }
    }

    private companion object {

        fun createGeometry(points: List<Vector3fc>): BufferGeometry {
            return BufferGeometry().apply {
                addAttribute("position", FloatBufferAttribute(points.flatten(), 3))
            }
        }

    }

}

class ThreektHeightmapProxy(
    ctx: RenderContext,
    width: Float,
    height: Float,
    widthSegments: Int,
    heightSegments: Int,
    heights: FloatArray
) : ThreektProxy(ctx) {

    init {

        val map = PlaneBufferGeometry(width, height, widthSegments, heightSegments)

        var i = 0
        var j = 0
        val vertices = map.getAttribute("position") as FloatBufferAttribute
        while (i < vertices.size) {
            vertices[i + 2] = heights[j++]
            i += 3
        }

        val mesh = Mesh(map).apply {
            material.side = Side.Double
        }

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

class ThreektWaterProxy(
    ctx: RenderContext,
    width: Float,
    height: Float
) : ThreektProxy(ctx) {

    val water: Water

    init {

        val waterNormals = javaClass.classLoader.getResource("textures/waternormals.jpg")!!

        val texture = TextureLoader.load(waterNormals).apply {
            wrapS = TextureWrapping.Repeat
            wrapT = TextureWrapping.Repeat
        }

        val planeGeometry = PlaneBufferGeometry(width, height)
        water = Water(
            planeGeometry, Water.Options(
                alpha = 0.7f,
                waterNormals = texture,
                waterColor = Color(0x001e0f),
                sunColor = Color(0xffffff),
                textureWidth = 512,
                textureHeight = 512,
                sunDirection = Vector3(1, 1, 0).normalize(),
                distortionScale = 1f
            )
        )
        water.rotateX(-PI.toFloat() / 2)
        ctx.invokeLater {
            attachChild(water)
        }

    }

}

class ThreektAxisProxy(
    ctx: RenderContext,
    size: Float
) : ThreektProxy(ctx) {

    private val axis = AxesHelper(
        size = size
    )

    init {
        ctx.invokeLater {
            attachChild(axis)
        }
    }

}

class ThreektArrowProxy(
    ctx: RenderContext,
    length: Float
) : ThreektProxy(ctx) {

    private val arrow = ArrowHelper(length = length)

    init {
        ctx.invokeLater {
            attachChild(arrow)
        }
    }

    fun setLength(length: Float) {
        arrow.setLength(length)
    }

}
