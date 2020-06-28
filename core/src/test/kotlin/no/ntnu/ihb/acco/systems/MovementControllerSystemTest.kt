package no.ntnu.ihb.acco.systems

import no.ntnu.ihb.acco.components.Controllable
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.MovementController
import no.ntnu.ihb.acco.render.PerspectiveCamera
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import no.ntnu.ihb.acco.render.shape.BoxShape


object MovementControllerSystemTest {

    @JvmStatic
    fun main(args: Array<String>) {

        Engine().also { engine ->

            val spacing = 2.0

            val e1 = engine.createEntity(
                TransformComponent().apply {
                    localTranslateX(spacing * 0.5)
                },
                GeometryComponent(BoxShape()),
                Controllable()
            )

            val e2 = engine.createEntity(
                TransformComponent().apply {
                    localTranslateX(-spacing * 0.5)
                },
                GeometryComponent(BoxShape())
            )

            engine.createEntity(
                TransformComponent().apply {
                    setLocalTranslation(0.0, 0.0, -10.0)
                },
                PerspectiveCamera()
            )


            engine.addSystem(JmeRenderSystem())
            engine.addSystem(MovementController())

            engine.invokeAt(5.0) {
                e1.removeComponent<Controllable>()
                e2.addComponent(Controllable())
            }

            engine.runner.start()

        }

    }

}
