package info.laht.acco.physics.bullet

import info.laht.acco.components.TransformComponent
import info.laht.acco.core.Engine
import info.laht.acco.core.Entity
import info.laht.acco.physics.ColliderComponent
import info.laht.acco.physics.RigidBodyComponent
import info.laht.acco.render.GeometryComponent
import info.laht.acco.render.jme.JmeEngineRunner
import info.laht.acco.render.shape.PlaneShape

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
