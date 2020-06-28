package no.ntnu.ihb.acco.systems

import no.ntnu.ihb.acco.components.Controllable
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
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

            val e1 = Entity().apply {
                transform.localTranslateX(-spacing * 0.5)
                addComponent(GeometryComponent(BoxShape()))
                addComponent(Controllable())
                engine.addEntity(this)
            }

            val e2 = Entity().apply {
                transform.localTranslateX(spacing * 0.5)
                addComponent(GeometryComponent(BoxShape()))
                engine.addEntity(this)
            }

            Entity().apply {
                transform.setLocalTranslation(0.0, 0.0, -10.0)
                addComponent(PerspectiveCamera())
                engine.addEntity(this)
            }

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
