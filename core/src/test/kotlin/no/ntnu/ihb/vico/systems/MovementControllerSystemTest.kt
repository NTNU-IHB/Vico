package no.ntnu.ihb.vico.systems

import info.laht.krender.mesh.BoxMesh
import info.laht.krender.mesh.SphereMesh
import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.Controllable
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import org.joml.Matrix4f


object MovementControllerSystemTest {

    @JvmStatic
    fun main(args: Array<String>) {

        val renderer = ThreektRenderer().apply {
            init(Matrix4f().setTranslation(0f, 0f, 5f))
        }

        execution {

            entities {

                val spacing = 2.0

                entity("e1") {

                    component {
                        Transform().apply {
                            frame.localTranslateX(spacing * 0.5)
                        }
                    }
                    component { Geometry(SphereMesh()) }
                    component { Controllable() }
                }

                entity("e2") {
                    component {
                        Transform().apply {
                            frame.localTranslateX(-spacing * 0.5)
                        }
                    }
                    component { Geometry(BoxMesh()) }
                }

                entity("e3") {
                    component {
                        Transform().apply {
                            frame.setLocalTranslation(0.0, 0.0, -10.0)
                        }
                    }
                    /*component {
                        PerspectiveCamera()
                    }*/
                }

            }

            systems {
                system { MovementController() }
                system { GeometryRenderer(renderer) }
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
