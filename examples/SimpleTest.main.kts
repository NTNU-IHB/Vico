#!kts

@file:Repository("file:///C:/Users/larsi/.m2/repository")
@file:Repository("https://dl.bintray.com/jmonkeyengine/org.jmonkeyengine")

@file:DependsOn("no.ntnu.ihb.vico:core:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:jme-render:0.1.0")

import no.ntnu.ihb.vico.components.TransformComponent
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.render.GeometryComponent
import no.ntnu.ihb.vico.render.jme.JmeRenderSystem
import no.ntnu.ihb.vico.shapes.BoxShape

Engine().use { engine ->

    val e1 = engine.createEntity("e1")
    e1.addComponent<TransformComponent>()
    e1.addComponent(GeometryComponent(BoxShape()))

    engine.addSystem(JmeRenderSystem())

    engine.runner.startAndWait()

}
