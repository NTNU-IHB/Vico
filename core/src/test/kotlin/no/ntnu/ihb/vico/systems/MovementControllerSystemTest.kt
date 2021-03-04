package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.components.Controllable
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh


object MovementControllerSystemTest {

    @JvmStatic
    fun main(args: Array<String>) {

        execution {

            entities {

                val spacing = 2.0

                entity("e1") {

                    component {
                        Transform().apply {
                            localTranslateX(spacing * 0.5)
                        }
                    }
                    component { Geometry(SphereMesh()).apply { color = ColorConstants.greenyellow } }
                    component { Controllable() }
                }

                entity("e2") {
                    component {
                        Transform().apply {
                            localTranslateX(-spacing * 0.5)
                        }
                    }
                    component { Geometry(BoxMesh()).apply { color = ColorConstants.lemonchiffon } }
                }

                entity("e3") {
                    component {
                        Transform().apply {
                            setLocalTranslation(0.0, 0.0, -10.0)
                        }
                    }
                }

            }

            systems {
                system { MovementController() }
                system { KtorServer(8000) }
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
