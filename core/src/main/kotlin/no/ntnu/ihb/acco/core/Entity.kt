package no.ntnu.ihb.acco.core


open class Entity internal constructor(
    name: String = ""
) {

    var tag: String? = null
    var name: String = name
        internal set

    private val mutableComponents: MutableList<Component> = mutableListOf()
    val components: List<Component>
        get() = mutableComponents

    private val componentMap: MutableMap<ComponentClazz, Component> = mutableMapOf()
    private val componentListeners: MutableList<ComponentListener> = mutableListOf()

    fun <E : Component> addComponent(componentClass: Class<out E>): E {
        return addComponent(instantiate(componentClass))
    }

    inline fun <reified E : Component> addComponent() = addComponent(E::class.java)

    fun <E : Component> addComponent(component: E): E {

        val componentClass = ComponentClazz(component::class.java)
        require(componentClass !in componentMap) {
            "Entity $name already contains component of type $componentClass!"
        }

        mutableComponents.add(component)
        componentMap[componentClass] = component
        componentListeners.forEach { it.onComponentAdded(this, component) }

        return component
    }

    private fun removeComponent(componentClass: ComponentClazz): Component {
        val myComponents by lazy {
            components.map { it.javaClass.toString() }
        }

        val component = componentMap[componentClass]
            ?: throw IllegalArgumentException(
                "No component of type $componentClass present! " +
                        "The following components are currently registered: $myComponents"
            )

        mutableComponents.remove(component)
        componentMap.remove(componentClass)
        componentListeners.forEach { it.onComponentRemoved(this, component) }
        return component
    }

    fun removeComponent(componentClass: ComponentClass): Component {
        return removeComponent(ComponentClazz(componentClass))
    }

    inline fun <reified E : Component> removeComponent(): Component {
        return removeComponent(E::class.java)
    }

    fun hasComponent(componentClass: ComponentClass): Boolean {
        return ComponentClazz(componentClass) in componentMap
    }

    inline fun <reified E : Component> hasComponent(): Boolean {
        return hasComponent(E::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Component> getComponent(componentClass: Class<E>): E {
        return componentMap[ComponentClazz(componentClass)] as E?
            ?: throw IllegalStateException("No component of type $componentClass registered with Entity named '$name'!")
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Component> getComponentOrNull(componentClass: Class<E>): E? {
        return componentMap[ComponentClazz(componentClass)] as E?
    }

    inline fun <reified E : Component> getComponent(): E {
        return getComponent(E::class.java)
    }

    inline fun <reified E : Component> getComponentOrNull(): E? {
        return getComponentOrNull(E::class.java)
    }

    fun removeAllComponents() {
        val comps = components.toMutableList()
        comps.forEach { c ->
            removeComponent(c::class.java)
        }
    }

    fun reset() {
        removeAllComponents()
        componentListeners.clear()
    }

    fun addComponentListener(listener: ComponentListener) {
        componentListeners.add(listener)
    }

    fun removeComponentListener(listener: ComponentListener) {
        componentListeners.remove(listener)
    }

    fun getProperties(): Collection<Property> {
        return components.flatMap { it.getProperties() }
    }

    fun getProperty(name: String): Property {
        val properties by lazy {
            getProperties().map { it.name }
        }
        return getPropertyOrNull(name)
            ?: throw NoSuchElementException("Could not find property named '$name'. Registered properties are $properties")
    }

    fun getPropertyOrNull(name: String): Property? {
        return getProperties().find { it.name == name }
    }

    fun getIntegerPropertyOrNull(name: String): IntProperty? {
        for (component in components) {
            val v = component.getIntegerPropertyOrNull(name)
            if (v != null) {
                return v
            }
        }
        return null
    }

    fun getIntegerProperties(): List<IntProperty> {
        return components.flatMap { it.ints }
    }

    fun getRealProperty(name: String): RealProperty {
        return getRealPropertyOrNull(name)
            ?: throw NoSuchElementException("No Real values property named '$name' found!")
    }

    fun getRealPropertyOrNull(name: String): RealProperty? {
        for (component in components) {
            val v = component.getRealPropertyOrNull(name)
            if (v != null) return v
        }
        return null
    }

    fun getRealProperties(): List<RealProperty> {
        return components.flatMap { it.reals }
    }

    fun getStringPropertyOrNull(name: String): StrProperty? {
        for (component in components) {
            val v = component.getStringPropertyOrNull(name)
            if (v != null) return v
        }
        return null
    }

    fun getStringProperties(): List<StrProperty> {
        return components.flatMap { it.strs }
    }

    fun getBooleanPropertyOrNull(name: String): BoolProperty? {
        for (component in components) {
            val v = component.getBooleanPropertyOrNull(name)
            if (v != null) return v
        }
        return null
    }

    fun getBooleanProperties(): List<BoolProperty> {
        return components.flatMap { it.bools }
    }

    override fun toString(): String {
        val componentClasses = components.map { it.javaClass.simpleName }
        return "Entity(name='$name', tag=$tag, components=$componentClasses)"
    }

}
