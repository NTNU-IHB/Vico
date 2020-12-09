#!kts

@file:Repository("mavenLocal")
@file:Repository("https://dl.bintray.com/laht/mvn")

@file:DependsOn("no.ntnu.ihb.vico:core:0.3.0", options = arrayOf("scope=compile,runtime"))
@file:DependsOn("no.ntnu.ihb.vico:threekt-render:0.3.0", options = arrayOf("scope=compile,runtime"))

@file:CompilerOptions("-jvm-target", "1.8")

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.render.mesh.BoxMesh

val renderer = ThreektRenderer()
EngineBuilder().renderer(renderer).build().use { engine ->

    val e1 = engine.createEntity("e1")
    e1.add<Transform>()
    e1.add(Geometry(BoxMesh()))

    engine.addSystem(GeometryRenderer())

    engine.runner.startAndWait()

}
