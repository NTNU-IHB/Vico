package no.ntnu.ihb.vico

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.render.mesh.PlaneMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import no.ntnu.ihb.vico.systems.PositionRefSystem
import org.joml.Matrix4f
import kotlin.math.PI

fun main() {

    val renderer = ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(0f, 0f, 10f))
    }

    EngineBuilder().stepSize(1.0 / 100).renderer(renderer).build().also { engine ->

        engine.addSystem(SlaveSystem())
        engine.addSystem(PositionRefSystem())
        engine.addSystem(GeometryRenderer())

        engine.createEntity("BouncingBall").also { slaveEntity ->

            val model = ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu"))
            SlaveComponent(model, "bb").apply {
                getRealProperty("h").write(doubleArrayOf(3.0))
                slaveEntity.addComponent(this)
            }

            slaveEntity.addComponent<Transform>()
            slaveEntity.addComponent(PositionRef(yRef = "h"))
            slaveEntity.addComponent(Geometry(SphereMesh()).apply {
                wireframe = true
                color = ColorConstants.blue
            })

        }

        engine.createEntity("Plane").also { planeEntity ->
            planeEntity.addComponent<Transform>()
            planeEntity.addComponent(Geometry(PlaneMesh(10f, 10f), Matrix4f().translate(0f, -0.5f, 0f).rotateX(PI.toFloat() / 2)))
        }

        engine.runner.apply {
            startAndWait()
        }

    }

}
