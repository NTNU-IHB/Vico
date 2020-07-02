#!vico run

Engine().use { engine ->

    val e1 = engine.createEntity("e1")
    e1.addComponent<TransformComponent>()
    e1.addComponent(GeometryComponent(BoxShape()))

    engine.addSystem(JmeRenderSystem())

    engine.runner.startAndWait()

}
