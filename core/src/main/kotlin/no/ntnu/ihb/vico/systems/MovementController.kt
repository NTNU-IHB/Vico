package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.Controllable
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.SimulationSystem

class MovementController : SimulationSystem(
    Family.all(Transform::class.java, Controllable::class.java).build()
) {

    override fun step(currentTime: Double, stepSize: Double) {

        entities.forEach { e ->
            val transform = e.get<Transform>()
            val controllable = e.get<Controllable>()

            if (engine.isKeyPressed("w")) {
                transform.localTranslateZ(controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed("s")) {
                transform.localTranslateZ(-controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed("a")) {
                transform.localTranslateX(controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed("d")) {
                transform.localTranslateX(-controllable.movementSpeed * stepSize)
            }
        }

    }

}
