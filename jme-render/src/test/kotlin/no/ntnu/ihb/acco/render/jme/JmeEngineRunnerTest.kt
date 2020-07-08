package no.ntnu.ihb.acco.render.jme

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.render.shape.CylinderShape
import no.ntnu.ihb.acco.render.shape.SphereShape
import no.ntnu.ihb.acco.systems.IteratingSystem
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.sin


private data class SineMoverComponent(
    var A: Double = 1.0,
    var f: Double = 0.1,
    var phi: Double = 0.0
) : Component() {

    fun compute(t: Double) = A * sin(TWO_PHI * f * t + phi)

    private companion object {
        private const val TWO_PHI = 2 * PI
    }


}

private class SineMoverSystem : IteratingSystem(
    Family.all(TransformComponent::class.java, SineMoverComponent::class.java).build()
) {

    private val tmp = Vector3d()

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

        val frame = entity.getComponent<TransformComponent>().frame
        val sc = entity.getComponent<SineMoverComponent>()
        frame.setLocalTranslation(frame.getLocalTranslation(tmp).apply { x = sc.compute(currentTime) })

    }

}

private fun e1(engine: Engine): Entity {
    return engine.createEntity("e1").also { e ->

        e.addComponent(TransformComponent()).apply {
            frame.setLocalTranslation(-1.0, 0.0, 0.0)
        }

        e.addComponent(GeometryComponent(BoxShape())).apply {
            setColor(Color.green)
        }
        e.addComponent(SineMoverComponent())
    }
}

fun main() {

    Engine().also { engine ->

        val e1 = e1(engine)
        engine.createEntity("e2").also { e ->

            val frame = e.addComponent(TransformComponent()).frame

            frame.setLocalTranslation(1.0, 0.0, 0.0)

            e.addComponent(GeometryComponent(SphereShape()).apply {
                setColor(Color.yellow)
            })
            e.addComponent(SineMoverComponent(f = 0.5))
            frame.setParent(e1.getComponent<TransformComponent>().frame)
            engine.getEntityByName("e1")
        }

        engine.addSystem(SineMoverSystem())
        engine.addSystem(JmeRenderSystem())

        engine.invokeAt(2.0) {
            engine.getEntityByName("e1").getComponent<GeometryComponent>().visible = false
            engine.getEntityByName("e2").getComponent<GeometryComponent>().wireframe = true

            engine.invokeIn(1.0) {
                engine.getEntityByName("e1").getComponent<GeometryComponent>().visible = true
                engine.getEntityByName("e2").getComponent<GeometryComponent>().wireframe = false
                engine.getEntityByName("e2").getComponent<GeometryComponent>().setColor(Color.red)
            }

        }

        engine.invokeAt(4.0) {

            engine.createEntity().apply {
                val frame = addComponent(TransformComponent()).frame
                frame.setLocalTranslation(0.0, 2.0, 0.0)
                addComponent(GeometryComponent(CylinderShape()).apply {
                    setColor(Color.blue)
                })
                frame.setParent(engine.getEntityByName("e2").getComponent<TransformComponent>().frame)
            }

            engine.removeEntity(engine.getEntityByName("e1"))

        }

        engine.runner.apply {
            enableRealTimeTarget = true
            start()
        }

    }

}
