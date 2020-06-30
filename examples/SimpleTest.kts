#!vico run

import no.ntnu.ihb.acco.components.*
import no.ntnu.ihb.acco.core.*
import no.ntnu.ihb.acco.render.*
import no.ntnu.ihb.acco.render.jme.*
import no.ntnu.ihb.acco.render.shape.*

Engine().use { engine ->

    val e1 = engine.createEntity("e1")
    e1.addComponent<TransformComponent>()
    e1.addComponent(GeometryComponent(BoxShape()))

    engine.addSystem(JmeRenderSystem())

    engine.runner.startAndWait()

}
