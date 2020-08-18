package no.ntnu.ihb.vico.core


typealias ComponentClass = Class<out Component>

abstract class Component : Properties() {
    open val name: String = javaClass.simpleName.decapitalize()
}

internal fun <E : Component> instantiate(componentClass: Class<out E>): E {
    val ctor = componentClass.getDeclaredConstructor()
    return ctor.newInstance()
}

internal class ComponentClazz(
    private val cls: Class<out Component>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComponentClazz

        if (!cls.isAssignableFrom(other.cls)) return false

        return true
    }

    override fun hashCode(): Int {

        if (cls == Component::class.java) {
            return cls.hashCode()
        } else {

            var clsHashObject: Class<*> = cls
            while (clsHashObject.superclass != Component::class.java) {
                clsHashObject = clsHashObject.superclass
            }
            return clsHashObject.hashCode()
        }
    }
}
