package no.ntnu.ihb.acco.dsl

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.*
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.scenario.ScenarioContext
import no.ntnu.ihb.acco.systems.PositionRefSystem


fun execution(ctx: ExecutionContext.() -> Unit): Engine {
    return ExecutionContext().apply(ctx).engine
}

class ExecutionContext {

    var startTime: Double? = null
    var stopTime: Double? = null
    var baseStepSize: Double? = null

    internal val engine: Engine by lazy {
        EngineBuilder()
            .startTime(startTime)
            .stopTime(stopTime)
            .stepSize(baseStepSize)
            .build()
    }

    fun entities(ctx: EntitiesContext.() -> Unit) {
        EntitiesContext(engine).apply(ctx)
    }

    fun systems(ctx: SystemsContext.() -> Unit) {
        SystemsContext(engine).apply(ctx)
    }

    fun scenario(ctx: ScenarioContext.() -> Unit) {
        engine.applyScenario(ScenarioContext().apply(ctx))
    }

}

class EntityContext(
    private val entity: Entity
) {

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

fun main() {

    execution {

        entities {

            entity {

                component {
                    TransformComponent()
                }
                component {
                    GeometryComponent(BoxShape()).apply { setColor(Color.blue) }
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

    }

}
