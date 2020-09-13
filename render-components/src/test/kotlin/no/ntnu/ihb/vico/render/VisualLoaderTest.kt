package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.render.jme.JmeRenderSystem
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Vector3d
import java.io.File
import kotlin.math.PI
import kotlin.math.sin

object VisualLoaderTest {

    private data class SineMoverComponent(
        var A: Double = 1.0,
        var f: Double = 0.1,
        var phi: Double = 0.0
    ) : Component() {

        var value: Double = 0.0
            private set

        init {
            registerProperties(RealLambdaProperty("value", 1,
                getter = { value }
            ))
        }

        fun compute(t: Double) = apply {
            value = A * sin(TWO_PHI * f * t + phi)
        }

        private companion object {
            private const val TWO_PHI = 2 * PI
        }

    }

    private class SineMoverSystem : IteratingSystem(
        Family.all(Transform::class.java, SineMoverComponent::class.java).build()
    ) {

        private val tmp = Vector3d()

        override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

            val frame = entity.getComponent<Transform>().frame
            val sc = entity.getComponent<SineMoverComponent>().compute(currentTime)
            frame.setLocalTranslation(frame.getLocalTranslation(tmp).apply { y = sc.value })

        }

    }

    @JvmStatic
    fun main(args: Array<String>) {

        val config = File(VisualLoaderTest.javaClass.classLoader.getResource("VisualConfig.xml")!!.file)

        Engine().also { engine ->

            engine.createEntity("SineMover").apply {
                addComponent(SineMoverComponent())
            }

            VisualLoader.load(config, engine)

            engine.addSystem(SineMoverSystem())
            engine.addSystem(JmeRenderSystem().apply { decimationFactor = 2 })

            engine.runner.apply {

                start()

            }

        }

    }

}
