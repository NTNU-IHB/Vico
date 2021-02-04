package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.InjectRenderer
import no.ntnu.ihb.vico.core.SimulationSystem
import no.ntnu.ihb.vico.render.mesh.*
import no.ntnu.ihb.vico.render.proxies.RenderProxy
import no.ntnu.ihb.vico.render.proxies.SpatialProxy

class GeometryRenderer : SimulationSystem(
        Family.all(Transform::class.java, Geometry::class.java).build()
) {

    @InjectRenderer
    private lateinit var renderer: RenderEngine

    private val proxies = mutableMapOf<Entity, RenderProxy>()

    init {
        priority = Int.MAX_VALUE
    }

    override fun postInit() {
        updateTransforms()
    }

    override fun entityAdded(entity: Entity) {
        entity.getOrNull<Geometry>()?.also { g ->
            val proxy = when (val shape = g.shape) {
                is SphereShape -> {
                    renderer.createSphere(shape.radius, g.offset)
                }
                is PlaneShape -> {
                    renderer.createPlane(shape.width, shape.height, g.offset)
                }
                is BoxShape -> {
                    renderer.createBox(shape.width, shape.width, shape.height, g.offset)
                }
                is CylinderShape -> {
                    renderer.createCylinder(shape.radius, shape.height, g.offset)
                }
                is CapsuleShape -> {
                    renderer.createCapsule(shape.radius, shape.height, g.offset)
                }
                is TrimeshShape -> {
                    renderer.createMesh(shape)
                }
                else -> null
            }
            proxy?.also { p ->

                p.setOpacity(g.opacity)
                p.setColor(g.color)
                p.setWireframe(g.wireframe)

                g.addEventListener("onVisibilityChanged") {
                    p.setVisible(it.value())
                }
                g.addEventListener("onWireframeChanged") {
                    p.setWireframe(it.value())
                }
                g.addEventListener("onColorChanged") {
                    p.setColor(it.value())
                }
                g.addEventListener("onOpacityChanged") {
                    p.setOpacity(it.value())
                }

                proxies[entity] = p
            }
        }
    }

    override fun entityRemoved(entity: Entity) {
        proxies.remove(entity)?.dispose()
    }

    private fun updateTransforms() {
        proxies.forEach { (e, p) ->
            val t = e.get<Transform>()

            p as SpatialProxy
            val m = t.getWorldMatrixf()
            p.setTransform(m)
        }
    }

    override fun step(currentTime: Double, stepSize: Double) {
        updateTransforms()
    }

    override fun close() {
        proxies.values.forEach {
            it.dispose()
        }
        proxies.clear()
    }

}
