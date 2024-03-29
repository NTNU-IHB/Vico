package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.util.PredicateTask
import java.util.function.Predicate

fun scenario(init: ScenarioContext.() -> Unit): ScenarioContext {
    return ScenarioContext().apply(init)
}

@Scoped
class ScenarioContext {

    private var initAction: ((Engine) -> Unit)? = null
    private val timedActions: MutableList<Pair<Double, (Engine) -> Unit>> = mutableListOf()
    private val predicateActions: MutableList<(Engine) -> PredicateTask> = mutableListOf()

    fun init(action: ActionContext.() -> Unit) {
        initAction = {
            action.invoke(ActionContext(it))
        }
    }

    fun invokeAt(timePoint: Number, action: ActionContext.() -> Unit) {
        timedActions.add(timePoint.toDouble() to {
            action.invoke(ActionContext(it))
        })
    }

    fun invokeWhen(action: WhenContext.() -> Unit) {
        predicateActions.add {
            val ctx = WhenContext(it).apply(action)
            object : PredicateTask {
                override fun test(it: Engine): Boolean {
                    return ctx.predicate.test(it)
                }

                override fun run() {
                    ctx.task.invoke()
                }
            }
        }
    }

    fun applyScenario(engine: Engine) {
        initAction?.apply { engine.initTask = this }
        timedActions.forEach { (timePoint, action) ->
            engine.invokeAt(timePoint) {
                action.invoke(engine)
            }
        }
        predicateActions.forEach { action ->
            engine.invokeWhen(action.invoke(engine))
        }
    }

}

@Scoped
open class ActionContext(
    private val engine: Engine
) {

    fun int(name: String) = IntContext(name)
    fun real(name: String) = RealContext(name)
    fun str(name: String) = StringContext(name)
    fun bool(name: String) = BooleanContext(name)

    private val pl = PropertyLocator(engine)

    fun addEntity(name: String? = null, ctx: EntityContext.() -> Unit) {
        val entity = engine.createEntity(name)
        EntityContext(entity).apply(ctx)
    }

    fun removeEntity(entityName: String) {
        val entity = engine.getEntityByName(entityName)
        engine.removeEntity(entity)
    }

    fun removeComponent(entityName: String, clazz: ComponentClass) {
        val entity = engine.getEntityByName(entityName)
        entity.remove(clazz)
    }

    fun addComponent(entityName: String, component: Component) {
        val entity = engine.getEntityByName(entityName)
        entity.add(component)
    }

    fun addComponent(entityName: String, component: ComponentClass) {
        val entity = engine.getEntityByName(entityName)
        entity.add(component)
    }

    inline fun <reified E : Component> addComponent(entityName: String) {
        addComponent(entityName, E::class.java)
    }

    inline fun <reified E : Component> removeComponent(entityName: String) {
        removeComponent(entityName, E::class.java)
    }

    fun addSystem(ctx: () -> BaseSystem) {
        engine.addSystem(ctx.invoke())
    }

    fun removeSystem(systemClazz: Class<out BaseSystem>) {
        engine.removeSystem(systemClazz)
    }

    inline fun <reified E : BaseSystem> removeSystem() = removeSystem(E::class.java)

    fun invokeAt(timePoint: Number, action: ActionContext.() -> Unit) {
        engine.invokeAt(timePoint.toDouble()) {
            action.invoke(ActionContext(engine))
        }
    }

    inner class IntContext internal constructor(
        val name: String
    ) {

        fun inc() {
            val property = pl.getIntegerProperty(name)
            property.write(property.read() + 1)
        }

        fun dec() {
            val property = pl.getIntegerProperty(name)
            property.write(property.read() - 1)
        }

        fun set(value: Int) {
            val property = pl.getIntegerProperty(name)
            property.write(value)
        }

        fun set(value: IntContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p2.read())
        }

        operator fun plusAssign(value: Int) {
            val property = pl.getIntegerProperty(name)
            property.write(property.read() + value)
        }

        operator fun plusAssign(value: IntContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() + p2.read())
        }

        operator fun minusAssign(value: Int) {
            val property = pl.getIntegerProperty(name)
            property.write(property.read() - value)
        }

        operator fun minusAssign(value: IntContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() - p2.read())
        }

        operator fun timesAssign(value: Int) {
            val property = pl.getIntegerProperty(name)
            property.write(property.read() * value)
        }

        operator fun timesAssign(value: IntContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() * p2.read())
        }

        operator fun timesAssign(value: RealContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write((p1.read() * p2.read()).toInt())
        }

        operator fun divAssign(value: Int) {
            val property = pl.getIntegerProperty(name)
            property.write(property.read() / value)
        }

        operator fun divAssign(value: IntContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() / p2.read())
        }

        operator fun divAssign(value: RealContext) {
            val p1 = pl.getIntegerProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write((p1.read() / p2.read()).toInt())
        }

    }

    inner class RealContext internal constructor(
        val name: String
    ) {

        fun set(value: Number) {
            val property = pl.getRealProperty(name)
            property.write(value.toDouble())
        }

        fun set(value: RealContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write(p2.read())
        }

        operator fun plusAssign(value: Number) {
            val property = pl.getRealProperty(name)
            val originalValue = property.read()
            property.write(originalValue + value.toDouble())
        }

        operator fun plusAssign(value: IntContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() + p2.read())
        }

        operator fun plusAssign(value: RealContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write(p1.read() + p2.read())
        }

        operator fun minusAssign(value: Number) {
            val property = pl.getRealProperty(name)
            property.write(property.read() - value.toDouble())
        }

        operator fun minusAssign(value: IntContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() - p2.read())
        }

        operator fun minusAssign(value: RealContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write(p1.read() - p2.read())
        }

        operator fun timesAssign(value: Number) {
            val property = pl.getRealProperty(name)
            property.write(property.read() * value.toDouble())
        }

        operator fun timesAssign(value: IntContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() * p2.read())
        }

        operator fun timesAssign(value: RealContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write(p1.read() * p2.read())
        }

        operator fun divAssign(value: Number) {
            val property = pl.getRealProperty(name)
            property.write(property.read() / value.toDouble())
        }

        operator fun divAssign(value: IntContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getIntegerProperty(value.name)
            p1.write(p1.read() / p2.read())
        }

        operator fun divAssign(value: RealContext) {
            val p1 = pl.getRealProperty(name)
            val p2 = pl.getRealProperty(value.name)
            p1.write(p1.read() / p2.read())
        }

    }

    inner class StringContext internal constructor(
        val name: String
    ) {

        fun set(value: String) {
            val property = pl.getStringProperty(name)
            property.write(value)
        }

        fun set(value: StringContext) {
            val p1 = pl.getStringProperty(name)
            val p2 = pl.getStringProperty(value.name)
            p1.write(p2.read())
        }

    }

    inner class BooleanContext internal constructor(
        val name: String
    ) {

        operator fun not() {
            val property = pl.getBooleanProperty(name)
            property.write(!property.read())
        }

        fun set(value: Boolean) {
            val property = pl.getBooleanProperty(name)
            property.write(value)
        }

        fun set(value: BooleanContext) {
            val p1 = pl.getBooleanProperty(name)
            val p2 = pl.getBooleanProperty(value.name)
            p1.write(p2.read())
        }

    }

}

class WhenContext(
    private val engine: Engine,
) {

    private var _task: (() -> Unit)? = null
    internal val task: () -> Unit
        get() = _task
            ?: throw IllegalStateException("Tasks not assigned! Did you forget to add `do` block after `when`?")

    private val pl = PropertyLocator(engine)
    internal lateinit var predicate: Predicate<Engine>

    fun `when`(p: Predicate<Engine>) {
        this.predicate = p
    }

    fun int(name: String): Int {
        val p = pl.getIntegerProperty(name)
        return p.read()
    }

    fun real(name: String): Double {
        val p = pl.getRealProperty(name)
        return p.read()
    }

    fun bool(name: String): Boolean {
        val p = pl.getBooleanProperty(name)
        return p.read()
    }

    fun str(name: String): String {
        val p = pl.getStringProperty(name)
        return p.read()
    }

    infix fun Unit.`do`(action: ActionContext.() -> Unit) {
        _task = {
            action.invoke(ActionContext(engine))
        }
    }

}
