package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.components.TransformComponent
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.systems.PositionRefSystem


fun execution(ctx: ExecutionContext.() -> Unit): Engine {
    return ExecutionContext().apply(ctx).engine
}

class ExecutionContext {

    var startTime: Number? = null
    var stopTime: Number? = null
    var baseStepSize: Double? = null

    internal val engine: Engine by lazy {
        EngineBuilder()
            .startTime(startTime?.toDouble())
            .stopTime(stopTime?.toDouble())
            .stepSize(baseStepSize)
            .build()
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
        engine.applyScenario(ScenarioContext().apply(ctx))
    }

}

class EntityContext(
    private val entity: Entity
) {

    fun component(component: Component) {
        entity.addComponent(component)
    }

    fun component(component: ComponentClass) {
        entity.addComponent(component)
    }

    inline fun <reified E : Component> component() {
        component(E::class.java)
    }

    fun component(ctx: () -> Component) {
        entity.addComponent(ctx.invoke())
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

    fun connection(ctx: () -> Connection) {
        engine.addConnection(ctx.invoke())
    }

    infix fun String.to(other: String): Connection {

        val p1 = UnboundProperty.parse(this).bounded(engine)
        val p2 = UnboundProperty.parse(other).bounded(engine)

        return ScalarConnection(
            Connector.inferConnectorType(p1.component, p1.property),
            Connector.inferConnectorType(p2.component, p2.property)
        )

    }

}


fun main() {

    execution {

        entities {

            entity {

                component {
                    TransformComponent()
                }

            }

        }

        systems {

            system {
                PositionRefSystem()
            }

        }

        scenario {

            invokeAt(2.0) {

            }

        }

        connections {
            "e1.value" to "e2.value"
            "p2.value" to "e2.c"
        }

    }

}
