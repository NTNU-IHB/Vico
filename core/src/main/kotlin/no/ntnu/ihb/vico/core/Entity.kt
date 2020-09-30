package no.ntnu.ihb.vico.core

@Suppress("unused")
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

    fun getComponentOrNull(componentName: String): Component? {
        return componentMap.values.firstOrNull { it.name == componentName }
    }

    fun getComponent(componentName: String): Component {
        return getComponentOrNull(componentName)
            ?: throw NoSuchElementException("No Component named '$componentName' in entity '$name'!")
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

    val properties: Collection<Property>
        get() {
            return components.flatMap { it.properties }
        }

    fun getProperty(name: String): Property {
        val properties by lazy {
            properties.map { it.name }
        }
        return getPropertyOrNull(name)
            ?: throw NoSuchElementException("Could not find property named '$name'. Registered properties are $properties")
    }

    fun getPropertyOrNull(name: String): Property? {
        return properties.find { it.name == name }
    }

    fun getIntegerPropertyOrNull(name: String): IntProperty? {
        val properties = components.mapNotNull {
            it.properties.getIntegerPropertyOrNull(name)
        }
        return when {
            properties.isEmpty() -> null
            properties.size == 1 -> properties.first()
            else -> throw IllegalStateException("Entity has multiple properties named: $name")
        }
    }

    fun getIntegerProperty(name: String): IntProperty {
        return getIntegerPropertyOrNull(name)
            ?: throw NoSuchElementException("No property named '$name' of type Integer could be located!")
    }

    fun getIntegerProperties(): List<IntProperty> {
        return components.flatMap { it.properties.ints }
    }

    fun getRealProperty(name: String): RealProperty {
        return getRealPropertyOrNull(name)
            ?: throw NoSuchElementException("No property named '$name' of type Real could be located!")
    }

    fun getRealPropertyOrNull(name: String): RealProperty? {
        val properties = components.mapNotNull {
            it.properties.getRealPropertyOrNull(name)
        }
        return when {
            properties.isEmpty() -> null
            properties.size == 1 -> properties.first()
            else -> throw IllegalStateException("Entity has multiple properties named: $name")
        }
    }

    fun getRealProperties(): List<RealProperty> {
        return components.flatMap { it.properties.reals }
    }

    fun getStringPropertyOrNull(name: String): StrProperty? {
        val properties = components.mapNotNull {
            it.properties.getStringPropertyOrNull(name)
        }
        return when {
            properties.isEmpty() -> null
            properties.size == 1 -> properties.first()
            else -> throw IllegalStateException("Entity has multiple properties named: $name")
        }
    }

    fun getStringProperty(name: String): StrProperty {
        return getStringPropertyOrNull(name)
            ?: throw NoSuchElementException("No property named '$name' of type String could be located!")
    }

    fun getStringProperties(): List<StrProperty> {
        return components.flatMap { it.properties.strs }
    }

    fun getBooleanPropertyOrNull(name: String): BoolProperty? {
        val properties = components.mapNotNull {
            it.properties.getBooleanPropertyOrNull(name)
        }
        return when {
            properties.isEmpty() -> null
            properties.size == 1 -> properties.first()
            else -> throw IllegalStateException("Entity has multiple properties named: $name")
        }
    }

    fun getBooleanProperty(name: String): BoolProperty {
        return getBooleanPropertyOrNull(name)
            ?: throw NoSuchElementException("No property named '$name' of type Boolean could be located!")
    }

    fun getBooleanProperties(): List<BoolProperty> {
        return components.flatMap { it.properties.bools }
    }

    override fun toString(): String {
        val componentClasses = components.map { it.javaClass.simpleName }
        return "Entity(name='$name', tag=$tag, components=$componentClasses)"
    }

}
