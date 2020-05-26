package no.ntnu.ihb.acco.core

class Entity(
    val name: String
) {

    var tag: String? = null

    private val mutableComponents = mutableListOf<Component>()
    val components: List<Component>
        get() = mutableComponents

    private val componentMap = mutableMapOf<Class<out Component>, Component>()
    private val componentListeners = mutableListOf<ComponentListener>()

    fun addComponent(component: Component) {
        mutableComponents.add(component)
        componentMap[component::class.java] = component
        componentListeners.forEach { l ->
            l.componentAdded(component)
        }
    }

    fun removeComponent(componentClass: Class<out Component>) {
        val component = componentMap[componentClass] ?: throw IllegalArgumentException()
        mutableComponents.remove(component)
        componentMap.remove(componentClass)
        componentListeners.forEach { l ->
            l.componentRemoved(component)
        }
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
            ?: throw IllegalStateException("Entity not have component: $componentClass")
    }

    internal fun addComponentListener(listener: ComponentListener) {
        this.componentListeners.add(listener)
    }

    internal fun removeComponentListeners(listener: ComponentListener) {
        this.componentListeners.remove(listener)
    }

}
