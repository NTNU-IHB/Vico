package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.render.RenderEngine


fun execution(ctx: ExecutionContext.() -> Unit): Engine {
    return ExecutionContext().apply(ctx).engine
}

class ExecutionContext {

    var startTime: Number? = null
    var stopTime: Number? = null
    var baseStepSize: Double? = null
    var renderEngine: RenderEngine? = null

    internal val engine: Engine by lazy {
        EngineBuilder()
                .startTime(startTime?.toDouble())
                .stopTime(stopTime?.toDouble())
                .stepSize(baseStepSize)
                .renderer(renderEngine)
                .build()
    }

    fun renderer(renderEngine: RenderEngine) {
        this.renderEngine = renderEngine
    }

    fun entities(ctx: EntitiesContext.() -> Unit) {
        EntitiesContext(engine).apply(ctx)
    }

    fun systems(ctx: SystemsContext.() -> Unit) {
        SystemsContext(engine).apply(ctx)
    }

    fun connections(ctx: ConnectionsContext.() -> Unit) {
        ConnectionsContext(engine).apply(ctx)
    }

    fun scenario(ctx: ScenarioContext.() -> Unit) {
        ScenarioContext().apply(ctx).applyScenario(engine)
    }

}

class EntityContext(
        private val entity: Entity
) {

    fun component(component: Component) {
        entity.add(component)
    }

    fun component(component: ComponentClass) {
        entity.add(component)
    }

    inline fun <reified E : Component> component() {
        component(E::class.java)
    }

    fun component(ctx: () -> Component) {
        entity.add(ctx.invoke())
    }

}

class EntitiesContext(
        private val engine: Engine
) {

    fun entity(name: String? = null, ctx: EntityContext.() -> Unit) {
        val entity = engine.createEntity(name)
        EntityContext(entity).apply(ctx)
    }

}

class SystemsContext(
        private val engine: Engine
) {

    fun system(ctx: () -> BaseSystem) {
        engine.addSystem(ctx.invoke())
    }

}

class ConnectionsContext(
        private val engine: Engine
) {

    infix fun String.to(other: String) {

        val p1 = UnboundProperty.parse(this).bounded(engine)
        val p2 = UnboundProperty.parse(other).bounded(engine)

        engine.addConnection(
                ScalarConnection(
                        Connector.inferConnectorType(p1.component, p1.property),
                        Connector.inferConnectorType(p2.component, p2.property)
                )
        )

    }

}
