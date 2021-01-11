package no.ntnu.ihb.vico.core


typealias ComponentClass = Class<out Component>

interface Component {

    val componentName: String
        get() = javaClass.simpleName.decapitalize()

    val properties: PropertyAccessor
        get() = Properties()

    val ints: Collection<IntProperty>
        get() = properties.ints
    val reals: Collection<RealProperty>
        get() = properties.reals
    val bools: Collection<BoolProperty>
        get() = properties.bools
    val strs: Collection<StrProperty>
        get() = properties.strs

    fun getProperty(name: String): Property = properties.getProperty(name)
    fun getPropertyOrNull(name: String): Property? = properties.getPropertyOrNull(name)
    fun getIntegerProperty(name: String): IntProperty = properties.getIntegerProperty(name)
    fun getIntegerPropertyOrNull(name: String): IntProperty? = properties.getIntegerPropertyOrNull(name)
    fun getRealProperty(name: String): RealProperty = properties.getRealProperty(name)
    fun getRealPropertyOrNull(name: String): RealProperty? = properties.getRealPropertyOrNull(name)
    fun getStringProperty(name: String): StrProperty = properties.getStringProperty(name)
    fun getStringPropertyOrNull(name: String): StrProperty? = properties.getStringPropertyOrNull(name)
    fun getBooleanProperty(name: String): BoolProperty = properties.getBooleanProperty(name)
    fun getBooleanPropertyOrNull(name: String): BoolProperty? = properties.getBooleanPropertyOrNull(name)

}

abstract class AbstractComponent(
        final override val properties: Properties = Properties()
) : Component

internal fun <E : Component> instantiate(componentClass: Class<out E>): E {
    val ctor = componentClass.getDeclaredConstructor()
    return ctor.newInstance()
}

internal class ComponentClazz(
        private val cls: ComponentClass
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComponentClazz

        if (!cls.isAssignableFrom(other.cls)) return false

        return true
    }

    override fun hashCode(): Int {

        return if (cls == Component::class.java) {
            cls.hashCode()
        } else {

            var clsHashObject: Class<*> = cls
            while (clsHashObject.superclass != null && clsHashObject.superclass != Component::class.java) {
                clsHashObject = clsHashObject.superclass
            }
            clsHashObject.hashCode()
        }
    }

}
