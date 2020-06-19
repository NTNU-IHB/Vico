package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.render.jaxb.*
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.render.shape.CapsuleShape
import no.ntnu.ihb.acco.render.shape.CylinderShape
import no.ntnu.ihb.acco.render.shape.SphereShape
import org.joml.Matrix4d
import java.io.File
import javax.xml.bind.JAXB

object VisualLoader {

    fun load(configFile: File): List<Entity> {
        require(configFile.exists())
        return load(JAXB.unmarshal(configFile, TVisualConfig::class.java))
    }

    fun load(config: TVisualConfig): List<Entity> {
        return config.transforms.transform.map { loadTransform(it) }
    }

    private fun loadTransform(t: TTransform): Entity {
        val entity = Entity(t.name)
        entity.addComponent(createGeometryComponent(t.geometry))
        return entity
    }

}

private fun createGeometryComponent(g: TGeometry): GeometryComponent {
    val shape = when {
        g.shape.sphere != null -> g.shape.sphere.toShape()
        g.shape.cylinder != null -> g.shape.cylinder.toShape()
        g.shape.capsule != null -> g.shape.capsule.toShape()
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

private fun TBox.toShape(): BoxShape {
    return BoxShape(xExtent * 0.5, yExtent * 0.5, zExtent * 0.5)
}

private fun TSphere.toShape(): SphereShape {
    return SphereShape(radius)
}

private fun TCylinder.toShape(): CylinderShape {
    return CylinderShape(radius, height)
}

private fun TCapsule.toShape(): CapsuleShape {
    return CapsuleShape(radius, height)
}
