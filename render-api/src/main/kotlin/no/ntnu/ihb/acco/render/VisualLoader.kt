package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.render.jaxb.*
import no.ntnu.ihb.acco.shapes.*
import org.joml.Matrix4d
import java.io.File
import javax.xml.bind.JAXB

object VisualLoader {

    fun load(configFile: File, engine: Engine) {
        require(configFile.exists())
        load(JAXB.unmarshal(configFile, TVisualConfig::class.java), engine)
    }

    fun load(config: TVisualConfig, engine: Engine) {
        config.transforms.transform.forEach { t ->
            val entity = engine.getEntityByName(t.name)
            entity.addComponent(createGeometryComponent(t.geometry))
        }
    }

    private fun createGeometryComponent(g: TGeometry): GeometryComponent {
        val shape = when {
            g.shape.box != null -> createShape(g.shape.box)
            g.shape.plane != null -> createShape(g.shape.plane)
            g.shape.sphere != null -> createShape(g.shape.sphere)
            g.shape.cylinder != null -> createShape(g.shape.cylinder)
            g.shape.capsule != null -> createShape(g.shape.capsule)
            else -> TODO()
        }
        val offset = Matrix4d()
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
            offset.setRotationXYZ(rx, ry, rz)
        }

        return GeometryComponent(shape, offset)
    }

    private fun createShape(b: TBox): BoxShape {
        return BoxShape(b.xExtent * 0.5, b.yExtent * 0.5, b.zExtent * 0.5)
    }

    private fun createShape(p: TPlane): PlaneShape {
        return PlaneShape(p.width, p.height)
    }

    private fun createShape(s: TSphere): SphereShape {
        return SphereShape(s.radius)
    }

    private fun createShape(c: TCylinder): CylinderShape {
        return CylinderShape(c.radius, c.height)
    }

    private fun createShape(c: TCapsule): CapsuleShape {
        return CapsuleShape(c.radius, c.height)
    }

}
