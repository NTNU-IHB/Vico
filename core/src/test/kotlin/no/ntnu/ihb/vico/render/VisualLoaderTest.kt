package no.ntnu.ihb.vico.render

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.math.TWO_PI
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Matrix4f
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

        val transform = entity.getComponent<Transform>()
        val sc = entity.getComponent<SineMover>().compute(currentTime)
        transform.setLocalTranslation(transform.getLocalTranslation(tmp).apply { y = sc.value })

    }

}


fun main() {

    val renderer = ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(0f, 0f, 5f))
    }
    val config = File(SineMover::class.java.classLoader.getResource("visualconfig/VisualConfig.xml")!!.file)

    EngineBuilder(
            renderEngine = renderer
    ).build().use { engine ->

        engine.createEntity("SineMover").apply {
            addComponent(SineMover())
        }

        VisualLoader.load(config, engine)

        engine.addSystem(SineMoverSystem())
        engine.addSystem(GeometryRenderer().apply { decimationFactor = 2 })

        engine.runner.startAndWait()

    }

}
