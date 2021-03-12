package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.math.Angle
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.Trail
import no.ntnu.ihb.vico.render.Water
import no.ntnu.ihb.vico.render.loaders.ObjLoader
import no.ntnu.ihb.vico.render.loaders.StlLoader
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.CylinderMesh
import no.ntnu.ihb.vico.render.mesh.Shape
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import org.joml.Matrix4f
import java.io.File

fun visualConfig(ctx: VisualConfig.() -> Unit): VisualConfig {
    return VisualConfig().apply(ctx)
}

class VisualConfig {

    private val transforms = mutableListOf<TransformContext>()

    fun transform(name: String? = null, parent: String? = null, ctx: TransformContext.() -> Unit) {
        transforms.add(TransformContext(name, parent).apply(ctx))
    }

    fun applyConfiguration(engine: Engine) {
        transforms.forEach { t ->

            val e = if (t.name.isNullOrEmpty()) {
                engine.createEntity(null)
            } else {
                engine.getEntityByNameOrNull(t.name) ?: engine.createEntity(t.name)
            }

            e.add<Transform>().apply {
                t.parent?.also { parentName ->
                    engine.getEntityByName(parentName).getOrNull<Transform>()?.also { parentTransform ->
                        setParent(parentTransform)
                    }
                }
            }

            e.add(t.geometry)

            t.trail?.also { e.add(it) }
            t.water?.also { e.add(it) }
            t.positionRef?.also { e.add(it) }

        }
    }
}


@Scoped
class TransformContext(
    val name: String? = null,
    val parent: String? = null
) {

    internal var water: Water? = null
    internal var trail: Trail? = null
    internal lateinit var geometry: Geometry
    internal var positionRef: PositionRef? = null

    fun geometry(ctx: GeometryContext.() -> Unit) {
        GeometryContext().apply(ctx).also { g ->
            geometry = Geometry(g.shape, g.offset).apply {
                this.color = g.color
                this.wireframe = g.wireframe
            }
        }
    }

    fun positionRef(ctx: PositionRefContext.() -> Unit) {
        PositionRefContext().apply(ctx).also { ref ->
            positionRef = PositionRef(
                ref.xGetter,
                ref.yGetter,
                ref.zGetter
            )
        }
    }

    fun trail(color: Int? = null) {
        this.trail = Trail(color = color)
    }

    fun water(size: Number) {
        this.water = Water(size)
    }

    fun water(width: Number, height: Number) {
        this.water = Water(width, height)
    }

}

class PositionRefContext {

    internal var xGetter: ValueProvider? = null
    internal var yGetter: ValueProvider? = null
    internal var zGetter: ValueProvider? = null

    fun x(ctx: DataContext.() -> ValueProvider) {
        xGetter = DataContext.let(ctx)
    }

    fun y(ctx: DataContext.() -> ValueProvider) {
        yGetter = DataContext.let(ctx)
    }

    fun z(ctx: DataContext.() -> ValueProvider) {
        zGetter = DataContext.let(ctx)
    }

}


@Scoped
class GeometryContext(
    val wireframe: Boolean = false,
    val color: Int = ColorConstants.gray
) {

    internal lateinit var shape: Shape
    internal var offset: Matrix4f? = null

    fun offsetPosition(x: Number, y: Number, z: Number) {
        if (offset == null) {
            offset = Matrix4f()
        }
        offset!!.setTranslation(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun offsetRotation(x: Number, y: Number, z: Number, unit: Angle.Unit = Angle.Unit.DEG) {
        if (offset == null) {
            offset = Matrix4f()
        }

        offset!!.setRotationXYZ(
            unit.ensureRadians(x.toFloat()),
            unit.ensureRadians(y.toFloat()),
            unit.ensureRadians(z.toFloat())
        )
    }

    fun shape(ctx: ShapeContext.() -> Unit) {
        ShapeContext().apply(ctx).also {
            this.shape = it.shape
        }
    }

}

@Scoped
class ShapeContext {

    internal lateinit var shape: Shape

    fun box(size: Number) {
        shape = BoxMesh(size.toFloat())
    }

    fun box(x: Number, y: Number, z: Number) {
        shape = BoxMesh(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun sphere(radius: Number) {
        shape = SphereMesh(radius.toFloat())
    }

    fun cylinder(radius: Number, height: Number) {
        shape = CylinderMesh(radius.toFloat(), height.toFloat())
    }

    fun mesh(source: String) {
        val file = File(source)
        require(file.exists())
        shape = when (val ext = file.extension) {
            "obj" -> ObjLoader().load(file)
            "stl" -> StlLoader().load(file)
            else -> throw IllegalArgumentException("Unknown file format: .$ext")
        }
    }

}

