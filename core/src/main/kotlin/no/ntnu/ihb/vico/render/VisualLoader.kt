package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.CylinderMesh
import no.ntnu.ihb.vico.render.mesh.PlaneMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import org.joml.Matrix4f
import java.io.File
import javax.xml.bind.JAXB

object VisualLoader {

    fun load(configFile: File, engine: Engine) {
        require(configFile.exists()) { "No such file: ${configFile.absolutePath}" }
        load(JAXB.unmarshal(configFile, TVisualConfig::class.java), engine)
    }

    fun load(config: TVisualConfig, engine: Engine) {
        config.transforms.transform.forEach { t ->
            engine.getEntityByName(t.name).apply {
                if (!has<Transform>()) {
                    add<Transform>()
                }
                add(createGeometry(t.geometry))
            }
        }
    }

    private fun createGeometry(g: TGeometry): Geometry {
        val shape = when {
            g.shape.box != null -> createShape(g.shape.box)
            g.shape.plane != null -> createShape(g.shape.plane)
            g.shape.sphere != null -> createShape(g.shape.sphere)
            g.shape.cylinder != null -> createShape(g.shape.cylinder)
            else -> TODO()
        }
        val offset = Matrix4f()
        g.offsetPosition?.also { p ->
            offset.setTranslation(p.px, p.py, p.pz)
        }
        g.offsetRotation?.also { r ->
            var rx = r.x
            var ry = r.y
            var rz = r.z
            if (g.offsetRotation.repr == TAngleRepr.DEG) {
                rx = Math.toRadians(rx)
                ry = Math.toRadians(ry)
                rz = Math.toRadians(rz)
            }
            offset.setRotationXYZ(rx.toFloat(), ry.toFloat(), rz.toFloat())
        }

        return Geometry(shape, offset).also {

            g.color?.also { c ->
                val color = if (c.startsWith("#")) {
                    Integer.decode(c)
                } else {
                    ColorConstants.getByName(c)
                }
                it.color = color
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

}
