package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.render.proxies.LineProxy
import org.joml.Vector3d
import org.joml.Vector3f

class Trail @JvmOverloads constructor(
        length: Float? = null,
        color: Int? = null
) : Component {

    val length: Float = length ?: 10f
    val color: Int = color ?: ColorConstants.blue

}

class TrailRenderer : SimulationSystem(
        Family.all(Transform::class.java, Trail::class.java).build()
) {

    @InjectRenderer
    private lateinit var renderer: RenderEngine

    private val proxies: MutableMap<Entity, Pair<LineProxy, MutableList<Vector3f>>> = mutableMapOf()

    init {
        priority = Int.MAX_VALUE
    }

    override fun entityAdded(entity: Entity) {
        entity.getOrNull<Trail>()?.also { t ->
            val proxy = renderer.createLine(emptyList()).apply {
                setColor(t.color)
            }
            proxies[entity] = proxy to mutableListOf()
        }
    }

    override fun entityRemoved(entity: Entity) {
        proxies.remove(entity)?.first?.dispose()
    }

    private fun updateTrails() {
        proxies.forEach { (e, p) ->

            val t = e.get<Transform>()
            val v = Vector3f().set(t.getTranslation(Vector3d()))

            val points = p.second.apply {
                add(v)
            }
            //TODO compute line length

            if (points.size > 100) {
                points.removeAt(0)
            }

            p.first.update(points)
        }
    }

    override fun postInit() {
        updateTrails()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        updateTrails()
    }

    override fun close() {
        proxies.values.forEach {
            it.first.dispose()
        }
        proxies.clear()
    }

}
