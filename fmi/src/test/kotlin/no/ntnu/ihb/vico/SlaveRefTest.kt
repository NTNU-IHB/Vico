package no.ntnu.ihb.vico

import info.laht.krender.ColorConstants
import info.laht.krender.mesh.SphereMesh
import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.systems.PositionRefSystem
import org.joml.Matrix4f

fun main() {

    val renderer = ThreektRenderer().apply {
        init(Matrix4f().setTranslation(0f, 0f, 10f))
    }

    Engine(1.0 / 100).also { engine ->

        engine.addSystem(SlaveSystem())
        engine.addSystem(PositionRefSystem())
        engine.addSystem(GeometryRenderer(renderer))

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

        engine.runner.apply {
            startAndWait()
        }

    }

}
