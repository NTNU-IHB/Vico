package no.ntnu.ihb.vico.render

import info.laht.krender.RenderEngine
import info.laht.krender.mesh.*
import info.laht.krender.proxies.RenderProxy
import info.laht.krender.proxies.SpatialProxy
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.Inject
import no.ntnu.ihb.vico.core.SimulationSystem

class GeometryRenderer : SimulationSystem(
        Family.all(Transform::class.java, Geometry::class.java).build()
) {

    @Inject
    private lateinit var renderer: RenderEngine
    private val proxies = mutableMapOf<Entity, RenderProxy>()

    init {
        priority = Int.MAX_VALUE
    }

    override fun postInit() {
        updateTransforms()
    }

    override fun entityAdded(entity: Entity) {
        entity.getComponentOrNull<Geometry>()?.also { g ->
            val proxy = when (val shape = g.shape) {
                is PlaneShape -> {
                    renderer.createPlane(shape.width, shape.height, g.offset)
                }
                is BoxShape -> {
                    renderer.createBox(shape.width, shape.width, shape.height)
                }
                is SphereShape -> {
                    renderer.createSphere(shape.radius)
                }
                is CylinderShape -> {
                    renderer.createCylinder(shape.radius, shape.height)
                }
                is CapsuleShape ->
                    renderer.createCapsule(shape.radius, shape.height)
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
            val t = e.getComponent<Transform>()

            p as SpatialProxy
            val m = t.frame.getWorldMatrixf()
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
