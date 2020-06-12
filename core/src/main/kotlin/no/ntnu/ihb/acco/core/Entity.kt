package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.util.Node

data class Tag(
    val tag: String
)

class Entity(
    val name: String = "entity"
) {

    private val ints = mutableMapOf<String, IntVar>()
    private val reals = mutableMapOf<String, RealVar>()
    private val strs = mutableMapOf<String, StrVar>()
    private val bools = mutableMapOf<String, BoolVar>()

    var tag: Tag? = null
    val transform = TransformComponent()
    private var node: Node<Entity> = Node(this)

    private val mutableComponents = mutableListOf<Component>()
    val components: List<Component>
        get() = mutableComponents

    private val componentMap = mutableMapOf<Class<out Component>, Component>()
    private val componentListeners = mutableListOf<ComponentListener>()

    val hierarchicalName: String
        get() {
            val sb = StringBuilder().append(name)
            var current = node
            while (current.hasParent()) {
                current = current.parent!!
                sb.insert(0, current.data.name + '.')
            }
            return sb.toString()
        }

    init {
        addComponent(transform)
    }

    fun addComponent(component: Component) = apply {

        val componentClass = component::class.java
        require(componentClass !in componentMap) {
            "Entity $hierarchicalName already contains component of type $componentClass!"
        }

        mutableComponents.add(component)
        componentMap[componentClass] = component

        if (component is CosimulationComponent) {
            component.variables.forEach { (name, `var`) ->
                when (`var`) {
                    is IntVar -> ints[name] = `var`
                    is RealVar -> reals[name] = `var`
                    is StrVar -> strs[name] = `var`
                    is BoolVar -> bools[name] = `var`
                }
            }
        }

        componentListeners.forEach { l ->
            l.componentAdded(component)
        }
    }

    fun removeComponent(componentClass: Class<out Component>): Component {
        val component = componentMap[componentClass]
            ?: throw IllegalArgumentException("No component of type $componentClass registered!")
        mutableComponents.remove(component)
        componentMap.remove(componentClass)

        if (component is CosimulationComponent) {
            component.variables.forEach { (name, `var`) ->
                when (`var`) {
                    is IntVar -> ints.remove(name)
                    is RealVar -> reals.remove(name)
                    is StrVar -> strs.remove(name)
                    is BoolVar -> bools.remove(name)
                }
            }
        }

        componentListeners.forEach { l ->
            l.componentRemoved(component)
        }
        return component
    }

    fun hasComponent(componentClass: Class<out Component>): Boolean {
        return componentClass in componentMap
    }

    fun removeAllComponents() {
        val comps = components.toMutableList()
        comps.forEach { c ->
            removeComponent(c::class.java)
        }
    }

    fun reset() {
        componentListeners.clear()
        removeAllComponents()
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Component> getComponent(componentClass: Class<E>): E {
        return componentMap[componentClass] as E?
            ?: throw IllegalStateException("Entity does not have component: $componentClass")
    }

    inline fun <reified E : Component> getComponent(): E {
        return getComponent(E::class.java)
    }

    internal fun addComponentListener(listener: ComponentListener) {
        this.componentListeners.add(listener)
    }

    internal fun removeComponentListeners(listener: ComponentListener) {
        this.componentListeners.remove(listener)
    }

    fun getIntegerVariable(name: String): IntVar {
        return ints[name] ?: throw IllegalStateException("No variable named '$name' of type Int registered!")
    }

    fun getRealVariable(name: String): RealVar {
        return reals[name] ?: throw IllegalStateException("No variable named '$name' of type Real registered!")
    }

    fun getStringVariable(name: String): StrVar {
        return strs[name] ?: throw IllegalStateException("No variable named '$name' of type String registered!")
    }

    fun getBooleanVariable(name: String): BoolVar {
        return bools[name] ?: throw IllegalStateException("No variable named '$name' of type Boolean registered!")
    }

}
