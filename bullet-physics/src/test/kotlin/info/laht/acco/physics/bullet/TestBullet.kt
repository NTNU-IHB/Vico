package info.laht.acco.physics.bullet

import info.laht.acco.core.Engine
import info.laht.acco.core.Entity
import info.laht.acco.physics.ColliderComponent
import info.laht.acco.physics.RigidBodyComponent
import info.laht.acco.render.GeometryComponent
import info.laht.acco.render.TransformComponent
import info.laht.acco.render.jme.JmeEngineRunner
import info.laht.acco.render.shape.PlaneShape
import info.laht.acco.render.shape.SphereShape

fun main() {

    Engine(1.0 / 100).also { engine ->

        Entity("plane").also { e ->
            e.addComponent(TransformComponent().apply {
                setLocalTranslation(0.0, 0.0, 0.0)
            })
            e.addComponent(RigidBodyComponent())
            e.addComponent(ColliderComponent(PlaneShape()))
            e.addComponent(GeometryComponent(PlaneShape()))
            engine.addEntity(e)
        }

        Entity("sphere").also { e ->
            e.addComponent(TransformComponent().apply {
                setLocalTranslation(0.0, 5.0, 0.0)
            })
            e.addComponent(RigidBodyComponent(10.0))
            e.addComponent(ColliderComponent(SphereShape()))
            e.addComponent(GeometryComponent(SphereShape()))
            engine.addEntity(e)
        }

        engine.addSystem(BulletSystem())

        JmeEngineRunner(engine).apply {
            start()
        }

    }

}
