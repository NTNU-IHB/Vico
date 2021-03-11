package no.ntnu.ihb.vico.render

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.math.TWO_PI
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Vector3d
import java.io.File
import kotlin.math.sin


private data class SineMover(
    var A: Double = 1.0,
    var f: Double = 0.1,
    var phi: Double = 0.0
) : AbstractComponent() {

    var value: Double = 0.0
        private set

    init {
        properties.registerProperties(RealLambdaProperty("value", 1,
            getter = { value }
        ))
    }

    fun compute(t: Double) = apply {
        value = A * sin(TWO_PI * f * t + phi)
    }

}

private class SineMoverSystem : IteratingSystem(
    Family.all(Transform::class.java, SineMover::class.java).build()
) {

    private val tmp = Vector3d()

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

        val transform = entity.get<Transform>()
        val sc = entity.get<SineMover>().compute(currentTime)
        transform.setLocalTranslation(transform.getLocalTranslation(tmp).apply { y = sc.value })

    }

}

fun main() {

    val cl = SineMover::class.java.classLoader
    val config = File(cl.getResource("visualconfig/VisualConfig.xml")!!.file)

    Engine().use { engine ->

        engine.createEntity("SineMover").apply {
            add(SineMover())
        }

        VisualLoader.load(config, engine)
        engine.addSystem(SineMoverSystem())
        engine.addSystem(KtorServer(8000))
        engine.addSystem(ThreektRenderer())

        engine.runner.startAndWait()

    }

}
