package no.ntnu.ihb.acco.scenario

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.EngineInfo
import no.ntnu.ihb.acco.util.extractEntityAndPropertyName


class Scenario {

    var endTime: Number? = null
    internal val actions = mutableListOf<Pair<Double, (Engine) -> Unit>>()

    fun invokeAt(timePoint: Double, action: ActionContext.() -> Unit) {
        actions.add(timePoint to {
            action.invoke(ActionContext(it))
        })
    }

    /*fun invokeWhen(action: WhenContext.() -> Pair<Predicate<Engine>, Runnable>) {
        action.invoke(WhenContext(engine))
    }*/

}

fun scenario(init: Scenario.() -> Unit): Scenario {
    return Scenario().apply(init)
}

open class ActionContext(
    private val engine: Engine
) {

    inner class Integer(
        name: String
    ) {

        private val propertyIdentifier = name.extractEntityAndPropertyName()

        fun set(value: Int) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getIntegerProperty(propertyIdentifier.propertyName)
            property.write(value)
        }

        fun set(value: Integer) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getIntegerProperty(propertyIdentifier.propertyName)
            val p2 = e2.getIntegerProperty(value.propertyIdentifier.propertyName)
            p1.write(p2.read().first())
        }

        operator fun plusAssign(value: Int) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getIntegerProperty(propertyIdentifier.propertyName)
            val originalValue = property.read().first()
            property.write(originalValue + value)
        }

        operator fun plusAssign(value: Integer) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getRealProperty(propertyIdentifier.propertyName)
            val p2 = e2.getRealProperty(value.propertyIdentifier.propertyName)
            p1.write(p1.read().first() + p2.read().first())
        }

        operator fun minusAssign(value: Int) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getIntegerProperty(propertyIdentifier.propertyName)
            val originalValue = property.read().first()
            property.write(originalValue - value)
        }

        operator fun minusAssign(value: Integer) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getRealProperty(propertyIdentifier.propertyName)
            val p2 = e2.getRealProperty(value.propertyIdentifier.propertyName)
            p1.write(p1.read().first() - p2.read().first())
        }

    }

    inner class Real(
        name: String
    ) {

        private val propertyIdentifier = name.extractEntityAndPropertyName()

        fun set(value: Number) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getRealProperty(propertyIdentifier.propertyName)
            property.write(value.toDouble())
        }

        fun set(value: Real) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getRealProperty(propertyIdentifier.propertyName)
            val p2 = e2.getRealProperty(value.propertyIdentifier.propertyName)
            p1.write(p2.read().first())
        }

        operator fun plusAssign(value: Number) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getRealProperty(propertyIdentifier.propertyName)
            val originalValue = property.read().first()
            property.write(originalValue + value.toDouble())
        }

        operator fun plusAssign(value: Real) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getRealProperty(propertyIdentifier.propertyName)
            val p2 = e2.getRealProperty(value.propertyIdentifier.propertyName)
            p1.write(p1.read().first() + p2.read().first())
        }

        operator fun minusAssign(value: Number) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getRealProperty(propertyIdentifier.propertyName)
            val originalValue = property.read().first()
            property.write(originalValue - value.toDouble())
        }

        operator fun minusAssign(value: Real) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getRealProperty(propertyIdentifier.propertyName)
            val p2 = e2.getRealProperty(value.propertyIdentifier.propertyName)
            p1.write(p1.read().first() - p2.read().first())
        }

        operator fun timesAssign(value: Number) {
            val entity = engine.getEntityByName(propertyIdentifier.entityName)
            val property = entity.getRealProperty(propertyIdentifier.propertyName)
            val originalValue = property.read().first()
            property.write(originalValue * value.toDouble())
        }

        operator fun timesAssign(value: Real) {
            val e1 = engine.getEntityByName(propertyIdentifier.entityName)
            val e2 = engine.getEntityByName(value.propertyIdentifier.entityName)
            val p1 = e1.getRealProperty(propertyIdentifier.propertyName)
            val p2 = e2.getRealProperty(value.propertyIdentifier.propertyName)
            p1.write(p1.read().first() * p2.read().first())
        }

    }

    fun real(name: String) = Real(name)

    /*infix fun String.assign(value: Double) {
        val entity = engine.getEntityByName(entityName)
        val property = entity.getRealProperty(this)
        property.write(value)
    }

    infix fun String.add(value: Double) {
        val entity = engine.getEntityByName(entityName)
        val property = entity.getRealProperty(this)
        val currentValue = property.read().first()
        property.write(currentValue + value)
    }

    infix fun String.sub(value: Double) {
        val entity = engine.getEntityByName(entityName)
        val property = entity.getRealProperty(this)
        val currentValue = property.read().first()
        property.write(currentValue - value)
    }

    infix fun String.mul(value: Double) {
        val entity = engine.getEntityByName(entityName)
        val property = entity.getRealProperty(this)
        val currentValue = property.read().first()
        property.write(currentValue * value)
    }

    infix fun String.div(value: Double) {
        val entity = engine.getEntityByName(entityName)
        val property = entity.getRealProperty(this)
        val currentValue = property.read().first()
        property.write(currentValue / value)
    }
*/

/*
        inner class StringContext {

            infix fun String.assign(value: String) {
                val entity = engine.getEntityByName(entityName)
                val propertyName = this
                val property = entity.getStringProperty(propertyName)
                property.write(value)
            }

        }

        inner class BooleanContext {

            infix fun String.assign(value: String) {
                val entity = engine.getEntityByName(entityName)
                val propertyName = this
                val property = entity.getStringProperty(propertyName)
                property.write(value)
            }

        }*/


    class WhenContext(
        engine: Engine,
    ) : ActionContext(engine), EngineInfo by engine {

        infix fun Boolean.`do`(action: ActionContext.() -> Unit) {

        }

    }

}

fun main() {

    scenario {

        endTime = 90.0

        invokeAt(5.0) {

            real("bb.h") += real("bb.y")

        }

    }

}

