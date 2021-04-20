#!vico eval

@file:DependsOn("info.laht.vico:core:0.4.1")
@file:DependsOn("info.laht.vico:threekt-render:0.4.1")

@file:CompilerOptions("-jvm-target", "1.8")

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.mesh.BoxMesh

Engine().use { engine ->

    val e1 = engine.createEntity("e1")
    e1.add<Transform>()
    e1.add(Geometry(BoxMesh()))

    engine.addSystem(ThreektRenderer())

    engine.runner.startAndWait()

}
