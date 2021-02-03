package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.RealRef
import no.ntnu.ihb.vico.components.RotationRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.LinearTransform
import no.ntnu.ihb.vico.math.Angle
import no.ntnu.ihb.vico.render.loaders.ObjLoader
import no.ntnu.ihb.vico.render.loaders.StlLoader
import no.ntnu.ihb.vico.render.mesh.*
import no.ntnu.ihb.vico.systems.PositionRefSystem
import no.ntnu.ihb.vico.systems.RotationRefSystem
import org.joml.Matrix4f
import java.io.File
import javax.xml.bind.JAXB

object VisualLoader {

    fun load(configFile: File, engine: Engine) {
        require(configFile.exists()) { "No such file: ${configFile.absolutePath}" }
        load(JAXB.unmarshal(configFile, TVisualConfig::class.java), engine)
    }

    fun load(config: TVisualConfig, engine: Engine) {

        config.water?.also { w ->
            engine.createEntity("waterVisual", Water(w.width, w.height))
            engine.addSystem(WaterRenderer())
        }

        config.transform.forEach { t ->

            val e = engine.getEntityByNameOrNull(t.name) ?: engine.createEntity(t.name)

            val tc = e.getOrCreate<Transform>()
            e.add(createGeometry(t.geometry))

            t.positionRef?.also { ref ->
                e.add(
                    PositionRef(
                        xRef = ref.xRef.toRealRef(),
                        yRef = ref.yRef.toRealRef(),
                        zRef = ref.zRef.toRealRef()
                    )
                )
                if (!engine.hasSystem<PositionRefSystem>()) {
                    engine.addSystem(PositionRefSystem().apply { decimationFactor = config.decimationFactor * 2 })
                }
            }

            t.rotationRef?.also { ref ->
                e.add(
                    RotationRef(
                        xRef = ref.xRef.toRealRef(),
                        yRef = ref.yRef.toRealRef(),
                        zRef = ref.zRef.toRealRef(),
                        repr = if (ref.getRepr() == TAngleRepr.DEG) Angle.Unit.DEG else Angle.Unit.RAD
                    )
                )
                if (!engine.hasSystem<RotationRefSystem>()) {
                    engine.addSystem(RotationRefSystem().apply { decimationFactor = config.decimationFactor * 2 })
                }
            }

            t.trail?.also { trail ->
                e.add(Trail(trail.length, trail.color?.toColor()))
                if (!engine.hasSystem<TrailRenderer>()) {
                    engine.addSystem(TrailRenderer().apply { decimationFactor = config.decimationFactor * 5 })
                }
            }

            t.parent?.also { parent ->
                engine.getEntityByName(parent).apply {
                    val parentTransform = getOrCreate<Transform>()
                    tc.setParent(parentTransform)
                }
            }

        }

        engine.addSystem(GeometryRenderer().apply {
            decimationFactor = config.getDecimationFactor()
        })
    }

    private fun TRealRef?.toRealRef(): RealRef? {
        if (this == null) return null
        val linearTransformation = this.linearTransformation?.let {
            LinearTransform(factor = it.getFactor(), offset = it.getOffset())
        }
        return RealRef(this.name, linearTransformation)
    }

    private fun String.toColor(): Int {
        return if (startsWith("#")) {
            Integer.decode(this)
        } else {
            ColorConstants.getByName(this)
        }
    }

    private fun createGeometry(g: TGeometry): Geometry {
        val shape = when {
            g.shape.box != null -> createShape(g.shape.box)
            g.shape.plane != null -> createShape(g.shape.plane)
            g.shape.sphere != null -> createShape(g.shape.sphere)
            g.shape.cylinder != null -> createShape(g.shape.cylinder)
            g.shape.mesh != null -> createShape(g.shape.mesh)
            else -> TODO("Unsupported shape")
        }
        val offset = Matrix4f()
        g.offsetPosition?.also { p ->
            offset.setTranslation(p.getPx(), p.getPy(), p.getPz())
        }
        g.offsetRotation?.also { r ->
            var rx = r.getX()
            var ry = r.getY()
            var rz = r.getZ()
            if (g.offsetRotation.repr == TAngleRepr.DEG) {
                rx = Math.toRadians(rx)
                ry = Math.toRadians(ry)
                rz = Math.toRadians(rz)
            }
            offset.setRotationXYZ(rx.toFloat(), ry.toFloat(), rz.toFloat())
        }

        return Geometry(shape, offset).also {

            g.color?.also { c ->
                it.color = c.toColor()
            }

            g.wireframe?.also { w -> it.wireframe = w }
            g.opacity?.also { o -> it.opacity = o.toFloat() }

        }
    }

    private fun createShape(b: TBox): BoxMesh {
        return BoxMesh(b.xExtent, b.yExtent, b.zExtent)
    }

    private fun createShape(p: TPlane): PlaneMesh {
        return PlaneMesh(p.width, p.height)
    }

    private fun createShape(s: TSphere): SphereMesh {
        return SphereMesh(s.radius)
    }

    private fun createShape(c: TCylinder): CylinderMesh {
        return CylinderMesh(c.radius, c.height)
    }

    private fun createShape(c: TMesh): Trimesh {
        val source = File(c.source.toLowerCase())
        require(source.exists()) { "No such file: ${source.absolutePath}" }
        return when (source.extension) {
            "obj" -> ObjLoader().load((source))
            "stl" -> StlLoader().load((source))
            else -> TODO("Unsupported extension: ${source.extension}")
        }
    }

}
