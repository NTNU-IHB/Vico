package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.components.PositionRefComponent
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import no.ntnu.ihb.acco.shapes.SphereShape
import no.ntnu.ihb.acco.systems.PositionRefSystem
import no.ntnu.ihb.vico.model.ModelResolver

fun main() {

    Engine(1.0 / 100).also { engine ->

        engine.addSystem(SlaveSystem())
        engine.addSystem(PositionRefSystem())
        engine.addSystem(JmeRenderSystem())

        engine.createEntity("BouncingBall").also { slaveEntity ->

            val model = ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu"))
            SlaveComponent(model, "bb").apply {
                getRealProperty("h").write(doubleArrayOf(3.0))
                slaveEntity.addComponent(this)
            }

            slaveEntity.addComponent<TransformComponent>()
            slaveEntity.addComponent(PositionRefComponent(yRef = "h"))
            slaveEntity.addComponent(GeometryComponent(SphereShape()))

        }

        engine.runner.apply {
            start()
        }

    }

}
