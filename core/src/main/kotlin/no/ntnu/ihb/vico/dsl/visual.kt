package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.math.Angle
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.loaders.ObjLoader
import no.ntnu.ihb.vico.render.loaders.StlLoader
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.CylinderMesh
import no.ntnu.ihb.vico.render.mesh.Shape
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import org.joml.Matrix4f
import java.io.File

fun visual(ctx: VisualConfig.() -> Unit) {
    VisualConfig.apply(ctx)
}

object VisualConfig {

    private val transforms = mutableListOf<TransformContext>()

    fun transform(name: String? = null, ctx: TransformContext.() -> Unit) {
        transforms.add(TransformContext(name).apply(ctx))
    }

    internal fun apply(engine: Engine) {
        transforms.forEach { t ->

            val e = if (t.name.isNullOrEmpty()) {
                engine.createEntity(null)
            } else {
                engine.getEntityByNameOrNull(t.name) ?: engine.createEntity(t.name)
            }

            e.add(t.geometry)

        }
    }

}

@Scoped
class TransformContext(
    val name: String? = null
) {

    internal lateinit var geometry: Geometry

    fun geometry(ctx: GeometryContext.() -> Unit) {
        GeometryContext().apply(ctx).also { g ->
            geometry = Geometry(g.shape, g.offset).apply {
                this.color = g.color
                this.wireframe = g.wireframe
            }
        }
    }

    fun positionRef() {

    }

    fun rotationRef() {

    }

}

@Scoped
class GeometryContext {

    lateinit var shape: Shape
    var wireframe: Boolean = false
    var color: Int = ColorConstants.gray
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

    lateinit var shape: Shape

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
        shape = when (val ext = file.extension) {
            "obj" -> ObjLoader().load(file)
            "stl" -> StlLoader().load(file)
            else -> throw IllegalArgumentException("Unknown file format: .$ext")
        }
    }

}

