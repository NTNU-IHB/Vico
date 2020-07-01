package no.ntnu.ihb.acco.scenario

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.EngineInfo
import no.ntnu.ihb.acco.util.PredicateTask
import no.ntnu.ihb.acco.util.extractEntityAndPropertyName
import java.util.function.Predicate


class Scenario {

    var endTime: Number? = null
    internal val timedActions: MutableList<Pair<Double, (Engine) -> Unit>> = mutableListOf()
    internal val predicateActions: MutableList<(Engine) -> PredicateTask> = mutableListOf()

    fun invokeAt(timePoint: Double, action: ActionContext.() -> Unit) {
        timedActions.add(timePoint to {
            action.invoke(ActionContext(it))
        })
    }

    fun invokeWhen(action: WhenContext.() -> Unit) {
        predicateActions.add {
            val ctx = WhenContext(it).apply(action)
            object : PredicateTask {
                override fun test(engine: Engine): Boolean {
                    return ctx.predicate.test(engine)
                }

                override fun run() {
                    ctx.task.invoke()
                }
            }
        }
    }

}

fun scenario(init: Scenario.() -> Unit): Scenario {
    return Scenario().apply(init)
}

open class ActionContext(
    private val engine: Engine
) {

    abstract class PropertyContext(
        name: String
    ) {

        private val propertyIdentifier = name.extractEntityAndPropertyName()
        val entityName = propertyIdentifier.entityName
        val propertyName = propertyIdentifier.propertyName

    }

    inner class IntContext(
        name: String
    ) : PropertyContext(name) {

        fun set(value: Int) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getIntegerProperty(propertyName)
            property.write(value)
        }

        fun set(value: IntContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getIntegerProperty(propertyName)
            val p2 = e2.getIntegerProperty(value.propertyName)
            p1.write(p2.read().first())
        }

        operator fun plusAssign(value: Int) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getIntegerProperty(propertyName)
            val originalValue = property.read().first()
            property.write(originalValue + value)
        }

        operator fun plusAssign(value: IntContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write(p1.read().first() + p2.read().first())
        }

        operator fun minusAssign(value: Int) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getIntegerProperty(propertyName)
            val originalValue = property.read().first()
            property.write(originalValue - value)
        }

        operator fun minusAssign(value: IntContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write(p1.read().first() - p2.read().first())
        }

        operator fun timesAssign(value: IntContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getIntegerProperty(propertyName)
            val p2 = e2.getIntegerProperty(value.propertyName)
            p1.write(p1.read().first() * p2.read().first())
        }

        operator fun timesAssign(value: RealContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getIntegerProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write((p1.read().first() * p2.read().first()).toInt())
        }

    }

    inner class RealContext(
        name: String
    ) : PropertyContext(name) {

        fun set(value: Number) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getRealProperty(propertyName)
            property.write(value.toDouble())
        }

        fun set(value: RealContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write(p2.read().first())
        }

        operator fun plusAssign(value: Number) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getRealProperty(propertyName)
            val originalValue = property.read().first()
            property.write(originalValue + value.toDouble())
        }

        operator fun plusAssign(value: RealContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write(p1.read().first() + p2.read().first())
        }

        operator fun minusAssign(value: Number) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getRealProperty(propertyName)
            val originalValue = property.read().first()
            property.write(originalValue - value.toDouble())
        }

        operator fun minusAssign(value: RealContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write(p1.read().first() - p2.read().first())
        }

        operator fun timesAssign(value: Number) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getRealProperty(propertyName)
            val originalValue = property.read().first()
            property.write(originalValue * value.toDouble())
        }

        operator fun timesAssign(value: IntContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getIntegerProperty(value.propertyName)
            p1.write(p1.read().first() * p2.read().first())
        }

        operator fun timesAssign(value: RealContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getRealProperty(propertyName)
            val p2 = e2.getRealProperty(value.propertyName)
            p1.write(p1.read().first() * p2.read().first())
        }

    }

    inner class StringContext(
        name: String
    ) : PropertyContext(name) {

        fun set(value: String) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getStringProperty(propertyName)
            property.write(value)
        }

        fun set(value: StringContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getStringProperty(propertyName)
            val p2 = e2.getStringProperty(value.propertyName)
            p1.write(p2.read().first())
        }

    }

    inner class BooleanContext(
        name: String
    ) : PropertyContext(name) {

        fun set(value: Boolean) {
            val entity = engine.getEntityByName(entityName)
            val property = entity.getBooleanProperty(propertyName)
            property.write(value)
        }

        fun set(value: BooleanContext) {
            val e1 = engine.getEntityByName(entityName)
            val e2 = engine.getEntityByName(value.entityName)
            val p1 = e1.getBooleanProperty(propertyName)
            val p2 = e2.getBooleanProperty(value.propertyName)
            p1.write(p2.read().first())
        }

    }

    fun int(name: String) = RealContext(name)
    fun real(name: String) = RealContext(name)
    fun str(name: String) = StringContext(name)
    fun bool(name: String) = BooleanContext(name)

}

class WhenContext(
    val engine: Engine,
) : EngineInfo by engine {

    lateinit var predicate: Predicate<Engine>
    lateinit var task: () -> Unit

    fun predicate(p: Predicate<Engine>) {
        this.predicate = p
    }

    fun int(name: String): Int {
        val (entityName, propertyName) = name.extractEntityAndPropertyName()
        return engine.getEntityByName(entityName).getIntegerProperty(propertyName).read().first()
    }

    fun real(name: String): Double {
        val (entityName, propertyName) = name.extractEntityAndPropertyName()
        return engine.getEntityByName(entityName).getRealProperty(propertyName).read().first()
    }

    infix fun Unit.`do`(action: ActionContext.() -> Unit) {
        task = {
            action.invoke(ActionContext(engine))
        }
    }

}


fun main() {

    scenario {

        endTime = 90.0

        invokeAt(5.0) {
            real("bb.h") += real("bb.y")
        }

        invokeWhen {
            predicate {
                iterations == 2L
            }.`do` {
                real("bb.h") += real("bb.y")
            }
        }

    }

}

