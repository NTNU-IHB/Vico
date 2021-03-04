package no.ntnu.ihb.vico

import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.mesh.PlaneMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import no.ntnu.ihb.vico.systems.PositionRefSystem
import org.joml.Matrix4f
import kotlin.math.PI

fun main() {

    Engine(1.0 / 100).use { engine ->

        engine.addSystem(SlaveSystem())
        engine.addSystem(PositionRefSystem())
        engine.addSystem(KtorServer(8000))

        engine.createEntity("BouncingBall").also { slaveEntity ->

            val model = ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu"))
            SlaveComponent(model, "bb").apply {
                getRealProperty("h").write(doubleArrayOf(3.0))
                slaveEntity.add(this)
            }

            slaveEntity.add<Transform>()
            slaveEntity.add(PositionRef(yRef = "h"))
            slaveEntity.add(Geometry(SphereMesh()).apply {
                wireframe = true
                color = ColorConstants.blue
            })

        }

        engine.createEntity("Plane").also { planeEntity ->
            planeEntity.add<Transform>()
            planeEntity.add(Geometry(PlaneMesh(10f, 10f), Matrix4f().translate(0f, -0.5f, 0f).rotateX(PI.toFloat() / 2)))
        }

        engine.runner.apply {
            startAndWait()
        }

    }

}
