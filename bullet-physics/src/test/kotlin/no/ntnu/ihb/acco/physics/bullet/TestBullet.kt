package no.ntnu.ihb.acco.physics.bullet

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.physics.ColliderComponent
import no.ntnu.ihb.acco.physics.RigidBodyComponent
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.JmeEngineRunner
import no.ntnu.ihb.acco.render.shape.PlaneShape

fun main() {

    Engine(1.0 / 100).also { engine ->

        Entity("plane").also { e ->
            e.addComponent(TransformComponent().apply {
                position.set(1.0, 0.0, 0.0)
            })
            e.addComponent(RigidBodyComponent())
            e.addComponent(ColliderComponent(PlaneShape()))
            e.addComponent(GeometryComponent(PlaneShape()))
            engine.addEntity(e)
        }

        engine.addSystem(BulletSystem())

        JmeEngineRunner(engine).apply {
            start()
        }

    }

}
