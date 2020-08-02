package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.Controllable
import no.ntnu.ihb.vico.components.TransformComponent
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.render.GeometryComponent
import no.ntnu.ihb.vico.render.MovementController
import no.ntnu.ihb.vico.render.PerspectiveCamera
import no.ntnu.ihb.vico.render.jme.JmeRenderSystem
import no.ntnu.ihb.vico.shapes.BoxShape


object MovementControllerSystemTest {

    @JvmStatic
    fun main(args: Array<String>) {

        execution {

            entities {

                val spacing = 2.0

                entity("e1") {

                    component {
                        TransformComponent().apply {
                            frame.localTranslateX(spacing * 0.5)
                        }
                    }
                    component { GeometryComponent(BoxShape()) }
                    component { Controllable() }
                }

                entity("e2") {
                    component {
                        TransformComponent().apply {
                            frame.localTranslateX(-spacing * 0.5)
                        }
                    }
                    component { GeometryComponent(BoxShape()) }
                }

                entity("e3") {
                    component {
                        TransformComponent().apply {
                            frame.setLocalTranslation(0.0, 0.0, -10.0)
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

    }

}
