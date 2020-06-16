package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.components.TransformComponent
import java.util.*

typealias EntityTraverser = (Entity) -> Unit

enum class TraverseOption {
    DEPTH_FIRST, BREADTH_FIRST
}

open class Entity private constructor(
    name: String? = null,
    private val properties: Properties
) : PropertyAccessor by properties {

    internal var originalName = name ?: "Entity"
    var name = originalName
        private set

    var tag: String? = null
    val transform = TransformComponent()

    val numChildren: Int
        get() = children.size

    val numDescendants: Int
        get() = descendants.size

    private val mutableComponents = mutableListOf<Component>()
    val components: List<Component>
        get() = mutableComponents

    private val componentMap = mutableMapOf<Class<out Component>, Component>()
    private val componentListeners = mutableListOf<ComponentListener>()

    var parent: Entity? = null
        private set
    private val children: MutableList<Entity> = mutableListOf()
    private val descendants: MutableList<Entity> = mutableListOf()

    init {
        addComponent(transform)
    }

    @JvmOverloads
    constructor(name: String? = null) : this(name, Properties())

    fun addEntity(child: Entity) {
        require(child != this) { "Adding self!" }
        require(child !in descendants) { "$child is already a descendant" }
        children.add(child)
        child.parent = this
        child.transform.setParent(this.transform)
        if (tag != "root") {
            child.name = "${name}.${child.originalName}"
        }
        descendantAdded(child)
    }

    fun addAllEntities(child: Entity, vararg additionalChildren: Entity) {
        addEntity(child)
        additionalChildren.forEach { addEntity(it) }
    }

    fun removeEntity(child: Entity) {
        if (children.remove(child)) {
            child.parent = null
            child.transform.setParent(null)
            child.name = child.originalName
            descendantRemoved(child)
            child.removeAllComponents()
        }
    }

    fun removeAllEntities(child: Entity, vararg additionalChildren: Entity) {
        removeEntity(child)
        additionalChildren.forEach { removeEntity(it) }
    }

    protected open fun descendantAdded(entity: Entity) {
        descendants.add(entity)
        parent?.descendantAdded(entity)
    }

    protected open fun descendantRemoved(entity: Entity) {
        descendants.remove(entity)
        parent?.descendantRemoved(entity)
    }

    fun traverseAncestors(callback: EntityTraverser) {
        callback.invoke(this)
        parent?.also {
            it.traverseAncestors(callback)
        }
    }

    fun traverseDecendants(traverser: EntityTraverser, option: TraverseOption?) {
        when (option) {
            TraverseOption.BREADTH_FIRST -> breadthFirstTraversal(traverser)
            TraverseOption.DEPTH_FIRST -> depthFirstTraversal(traverser)
        }
    }

    fun depthFirstTraversal(traverser: EntityTraverser) {
        traverser.invoke(this)
        for (child in children) {
            child.depthFirstTraversal(traverser)
        }
    }

    fun breadthFirstTraversal(traverser: EntityTraverser) {
        traverser.invoke(this)
        val queue = ArrayDeque(children)
        while (!queue.isEmpty()) {
            queue.remove().also { child ->
                traverser.invoke(child)
                child.children.forEach { queue.add(it) }
            }
        }
    }

    fun addComponent(component: Component) = apply {

        val componentClass = component::class.java
        require(componentClass !in componentMap) {
            "Entity $name already contains component of type $componentClass!"
        }

        mutableComponents.add(component)
        componentMap[componentClass] = component

        if (component is CoSimulationComponent) {
            properties.add(component.variables)
        }

        componentListeners.forEach { l ->
            l.componentAdded(component)
        }
    }

    inline fun <reified E : Component> removeComponent(): Component {
        return removeComponent(E::class.java)
    }

    fun removeComponent(componentClass: Class<out Component>): Component {
        val component = componentMap[componentClass]
            ?: throw IllegalArgumentException("No component of type $componentClass registered!")
        mutableComponents.remove(component)
        componentMap.remove(componentClass)

        if (component is CoSimulationComponent) {
            properties.remove(component.variables)
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

    internal fun removeComponentListener(listener: ComponentListener) {
        this.componentListeners.remove(listener)
    }

    fun findInChildren(predicate: (Entity) -> Boolean): Entity {
        return children.first { predicate.invoke(it) }
    }

    fun findAllInChildren(predicate: (Entity) -> Boolean): List<Entity> {
        return children.filter { predicate.invoke(it) }
    }

    fun findInDescendants(predicate: (Entity) -> Boolean): Entity {
        return descendants.first { predicate.invoke(it) }
    }

    fun findAllInDescendants(predicate: (Entity) -> Boolean): List<Entity> {
        return descendants.filter { predicate.invoke(it) }
    }

    override fun toString(): String {
        return "Entity(name='$name', tag=$tag, numChildren=$numChildren, numDescendants=$numDescendants)"
    }

}
