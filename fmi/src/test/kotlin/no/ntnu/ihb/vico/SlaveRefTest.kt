package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.components.PositionRefComponent
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import no.ntnu.ihb.acco.render.shape.SphereShape
import no.ntnu.ihb.acco.systems.PositionRefSystem
import no.ntnu.ihb.fmi4j.writeReal
import no.ntnu.ihb.vico.model.ModelResolver

fun main() {

    Engine(1.0 / 100).also { engine ->

        engine.addSystem(SlaveSystem())
        engine.addSystem(PositionRefSystem())
        engine.addSystem(JmeRenderSystem())

        Entity("BouncingBall").also { slaveEntity ->
            val model = ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu"))
            SlaveComponent(model.instantiate("bb")).apply {
                writeReal("h", 3.0)
                slaveEntity.addComponent(this)
            }
            PositionRefComponent(yRef = "h").apply {
                slaveEntity.addComponent(this)
            }
            GeometryComponent(SphereShape()).apply {
                slaveEntity.addComponent(this)
            }
            engine.addEntity(slaveEntity)
        }

        engine.runner.apply {
            start()
        }

    }
}
