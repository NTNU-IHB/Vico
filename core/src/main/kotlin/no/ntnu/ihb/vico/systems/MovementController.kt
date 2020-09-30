package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.Controllable
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.SimulationSystem
import no.ntnu.ihb.vico.input.KeyStroke

class MovementController : SimulationSystem(
    Family.all(Transform::class.java, Controllable::class.java).build()
) {

    override fun step(currentTime: Double, stepSize: Double) {

        entities.forEach { e ->
            val transform = e.getComponent<Transform>()
            val controllable = e.getComponent<Controllable>()

            if (engine.isKeyPressed(KeyStroke.KEY_W)) {
                transform.localTranslateZ(controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed(KeyStroke.KEY_S)) {
                transform.localTranslateZ(-controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed(KeyStroke.KEY_A)) {
                transform.localTranslateX(controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed(KeyStroke.KEY_D)) {
                transform.localTranslateX(-controllable.movementSpeed * stepSize)
            }
        }

    }

}
