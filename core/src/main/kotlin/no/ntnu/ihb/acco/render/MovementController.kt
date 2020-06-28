package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.components.Controllable
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import no.ntnu.ihb.acco.input.KeyStroke

class MovementController : SimulationSystem(Family.all(Controllable::class.java).build()) {

    override fun step(currentTime: Double, stepSize: Double) {

        entities.forEach { e ->
            val transform = e.transform
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
