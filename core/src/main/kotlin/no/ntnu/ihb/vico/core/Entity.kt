package no.ntnu.ihb.vico.core

@Suppress("unused")
open class Entity private constructor(
        name: String? = null,
        private val mutableComponents: MutableList<Component>
) : Iterable<Component> by mutableComponents {

    var tag: String? = null
    var name: String = name ?: "Entity"
        internal set

    val properties: Collection<Property>
        get() {
            return flatMap { it.properties }
        }

    private val componentMap: MutableMap<ComponentClazz, Component> = mutableMapOf()
    private val componentListeners: MutableList<ComponentListener> = mutableListOf()

    internal constructor(name: String? = null) : this(name, mutableListOf())

    fun <E : Component> add(componentClass: Class<out E>): E {
        return add(instantiate(componentClass))
    }

    inline fun <reified E : Component> add() = add(E::class.java)

    fun <E : Component> add(component: E): E {

        val componentClass = ComponentClazz(component::class.java)
        require(componentClass !in componentMap) {
            "Entity $name already contains component of type $componentClass!"
        }

        mutableComponents.add(component)
        componentMap[componentClass] = component
        componentListeners.forEach { it.onComponentAdded(this, component) }

        return component
    }

    private fun remove(componentClass: ComponentClazz): Component {
        val myComponents by lazy {
            map { it.javaClass.toString() }
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

    fun remove(componentClass: ComponentClass): Component {
        return remove(ComponentClazz(componentClass))
    }

    inline fun <reified E : Component> remove(): Component {
        return remove(E::class.java)
    }

    fun has(componentClass: ComponentClass): Boolean {
        return ComponentClazz(componentClass) in componentMap
    }

    inline fun <reified E : Component> has(): Boolean {
        return has(E::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Component> get(componentClass: Class<E>): E {
        return componentMap[ComponentClazz(componentClass)] as E?
                ?: throw IllegalStateException("No component of type $componentClass registered with Entity named '$name'!")
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Component> getOrNull(componentClass: Class<E>): E? {
        return componentMap[ComponentClazz(componentClass)] as E?
    }

    inline fun <reified E : Component> get(): E {
        return get(E::class.java)
    }

    inline fun <reified E : Component> getOrNull(): E? {
        return getOrNull(E::class.java)
    }

    fun getOrNull(componentName: String): Component? {
        return componentMap.values.firstOrNull { it.name == componentName }
    }

    fun get(componentName: String): Component {
        return getOrNull(componentName)
                ?: throw NoSuchElementException("No Component named '$componentName' in entity '$name'!")
    }

    fun removeAll() {
        val comps = toMutableList()
        comps.forEach { c ->
            remove(c::class.java)
        }
    }

    fun reset() {
        removeAll()
        componentListeners.clear()
    }

    fun addComponentListener(listener: ComponentListener) {
        componentListeners.add(listener)
    }

    fun removeComponentListener(listener: ComponentListener) {
        componentListeners.remove(listener)
    }

    fun getProperty(name: String): Property {
        val properties by lazy {
            properties.map { it.name }
        }
        return getPropertyOrNull(name)
            ?: throw NoSuchElementException("Could not find property named '$name'. Registered properties are $properties")
    }

    internal fun getAllPropertiesNamed(name: String): List<Property> {
        return properties.filter { it.name == name }
    }

    fun getPropertyOrNull(name: String): Property? {
        return properties.find { it.name == name }
    }

    fun getIntegerPropertyOrNull(name: String): IntProperty? {
        val properties = mapNotNull {
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
        return flatMap { it.properties.ints }
    }

    fun getRealProperty(name: String): RealProperty {
        return getRealPropertyOrNull(name)
                ?: throw NoSuchElementException("No property named '$name' of type Real could be located!")
    }

    fun getRealPropertyOrNull(name: String): RealProperty? {
        val properties = mapNotNull {
            it.properties.getRealPropertyOrNull(name)
        }
        return when {
            properties.isEmpty() -> null
            properties.size == 1 -> properties.first()
            else -> throw IllegalStateException("Entity has multiple properties named: $name")
        }
    }

    fun getRealProperties(): List<RealProperty> {
        return flatMap { it.properties.reals }
    }

    fun getStringPropertyOrNull(name: String): StrProperty? {
        val properties = mapNotNull {
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
        return flatMap { it.properties.strs }
    }

    fun getBooleanPropertyOrNull(name: String): BoolProperty? {
        val properties = mapNotNull {
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
        return flatMap { it.properties.bools }
    }

    override fun toString(): String {
        val componentClasses = map { it.javaClass.simpleName }
        return "Entity(name='$name', tag=$tag, components=$componentClasses)"
    }

}
