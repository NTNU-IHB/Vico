package info.laht.acco.render.jme

import info.laht.acco.components.TransformComponent
import info.laht.acco.components.transform
import info.laht.acco.core.*
import info.laht.acco.render.Color
import info.laht.acco.render.GeometryComponent
import info.laht.acco.render.shape.BoxShape
import info.laht.acco.render.shape.SphereShape
import kotlin.math.PI
import kotlin.math.sin


private data class SineMoverComponent(
    var A: Double = 1.0,
    var f: Double = 0.1,
    var phi: Double = 0.0
) : Component {

    fun get(t: Double) = A * sin(TWO_PHI * f * t + phi)

    private companion object {
        private const val TWO_PHI = 2 * PI
    }

}

private class SineMoverSystem : IteratingSystem(
    Family.all(TransformComponent::class.java, SineMoverComponent::class.java).build()
) {

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {
        val sc = entity.getComponent(SineMoverComponent::class.java)
        val tc = entity.getComponent(TransformComponent::class.java)

        tc.position.y = sc.get(currentTime)

    }

}

fun main() {

    Engine().also { engine ->

        Entity("e1").also { e ->
            e.addComponent(TransformComponent().apply {
                position.set(-1.0, 0.0, 0.0)
            })
            e.addComponent(GeometryComponent(BoxShape()).apply {
                color.set(Color.green)
            })
            e.addComponent(SineMoverComponent())
            engine.addEntity(e)
        }
        Entity("e2").also { e ->
            e.addComponent(TransformComponent().apply {
                position.set(2.0, 0.0, 0.0)
            })
            e.addComponent(GeometryComponent(SphereShape()).apply {
                color.set(Color.yellow)
            })
            e.addComponent(SineMoverComponent(f = 0.5))
            engine.addEntity(e)
            engine.getEntityByName("e1").transform
                .attach(e.transform)
        }

        engine.addSystem(SineMoverSystem())

        JmeEngineRunner(engine).apply {
            start()
        }

        Thread.sleep(1000)
        engine.getEntityByName("e1").getComponent(GeometryComponent::class.java).visible = false
        engine.getEntityByName("e2").getComponent(GeometryComponent::class.java).wireframe = true
        Thread.sleep(1000)
        engine.entityManager.getEntityByName("e1").getComponent(GeometryComponent::class.java).visible = true
        engine.entityManager.getEntityByName("e2").getComponent(GeometryComponent::class.java).wireframe = false

    }

}
