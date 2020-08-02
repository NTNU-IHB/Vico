package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.components.Controllable
import no.ntnu.ihb.vico.components.TransformComponent
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.SimulationSystem
import no.ntnu.ihb.vico.input.KeyStroke

class MovementController : SimulationSystem(
    Family.all(TransformComponent::class.java, Controllable::class.java).build()
) {

    override fun step(currentTime: Double, stepSize: Double) {

        entities.forEach { e ->
            val transform = e.getComponent<TransformComponent>()
            val controllable = e.getComponent<Controllable>()

            if (engine.isKeyPressed(KeyStroke.KEY_W)) {
                transform.frame.localTranslateZ(controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed(KeyStroke.KEY_S)) {
                transform.frame.localTranslateZ(-controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed(KeyStroke.KEY_A)) {
                transform.frame.localTranslateX(controllable.movementSpeed * stepSize)
            }
            if (engine.isKeyPressed(KeyStroke.KEY_D)) {
                transform.frame.localTranslateX(-controllable.movementSpeed * stepSize)
            }
        }

    }

}
