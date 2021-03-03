package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.render.proxies.WaterProxy

class Water(
        val width: Float,
        val height: Float
) : Component


class WaterRenderer : SimulationSystem(
    Family.all(Water::class.java).build()
) {

    @InjectRenderer
    private var renderer: RenderEngine? = null
    private var waterProxy: WaterProxy? = null

    override fun entityAdded(entity: Entity) {
        if (waterProxy == null) {
            val water = entity.get<Water>()
            waterProxy = renderer?.createWater(water.width, water.height)
        }
    }

    override fun step(currentTime: Double, stepSize: Double) {
    }

    override fun close() {
        waterProxy?.dispose()
    }

}
