package no.ntnu.ihb.acco.core

class ComponentClazz(
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
