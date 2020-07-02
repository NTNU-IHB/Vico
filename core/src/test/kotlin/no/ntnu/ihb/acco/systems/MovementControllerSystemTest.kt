package no.ntnu.ihb.acco.systems

import no.ntnu.ihb.acco.components.Controllable
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.dsl.execution
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.MovementController
import no.ntnu.ihb.acco.render.PerspectiveCamera
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import no.ntnu.ihb.acco.render.shape.BoxShape


object MovementControllerSystemTest {

    @JvmStatic
    fun main(args: Array<String>) {

        execution {

            entities {

                val spacing = 2.0

                entity("e1") {

                    component {
                        TransformComponent().apply {
                            localTranslateX(spacing * 0.5)
                        }
                    }
                    component { GeometryComponent(BoxShape()) }
                    component { Controllable() }
                }

                entity("e2") {
                    component {
                        TransformComponent().apply {
                            localTranslateX(-spacing * 0.5)
                        }
                    }
                    component { GeometryComponent(BoxShape()) }
                }

                entity("e3") {
                    component {
                        TransformComponent().apply {
                            setLocalTranslation(0.0, 0.0, -10.0)
                        }
                    }
                    component {
                        PerspectiveCamera()
                    }
                }

            }

            systems {
                system { MovementController() }
                system { JmeRenderSystem() }
            }

            scenario {
                invokeAt(2.0) {
                    removeComponent<Controllable>("e1")
                    addComponent<Controllable>("e2")
                }
            }

        }.runner.startAndWait()


        /* Engine().also { engine ->

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

         }*/

    }

}
