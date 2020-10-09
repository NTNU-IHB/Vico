package no.ntnu.ihb.vico.core

import java.util.function.Predicate

class Family private constructor(
    all: List<ComponentClass>,
    one: List<ComponentClass>,
    exclude: List<ComponentClass>
) : Predicate<Entity> {

    private val all: List<ComponentClazz> = all.map { ComponentClazz(it) }
    private val one: List<ComponentClazz> = one.map { ComponentClazz(it) }
    private val exclude: List<ComponentClazz> = exclude.map { ComponentClazz(it) }

    override fun test(entity: Entity): Boolean {

        val componentClasses = entity.map {
            ComponentClazz(it::class.java)
        }

        if (!componentClasses.containsAll(all)) {
            return false
        }
        if (exclude.isNotEmpty()) {
            if (componentClasses.containsAll(exclude)) {
                return false
            }
        }

        if (one.isEmpty()) {
            return true
        }

        for (c in one) {
            if (c in componentClasses) {
                return true
            }
        }

        return false

    }

    companion object {

        val all = all().build()

        fun all(vararg c: ComponentClass): Builder {
            return Builder(all = c.toList())
        }

        fun one(vararg c: ComponentClass): Builder {
            return Builder(one = c.toList())
        }

        fun exclude(vararg c: ComponentClass): Builder {
            return Builder(exclude = c.toList())
        }
    }

    class Builder internal constructor(
        private var all: List<ComponentClass> = emptyList(),
        private var one: List<ComponentClass> = emptyList(),
        private var exclude: List<ComponentClass> = emptyList()
    ) {

        private val familyMap = mutableMapOf<Int, Family>()

        fun all(vararg c: ComponentClass) = apply {
            all = c.toList()
        }

        fun one(vararg c: ComponentClass) = apply {
            one = c.toList()
        }

        fun exclude(vararg c: ComponentClass) = apply {
            exclude = c.toList()
        }

        fun build(): Family {
            return familyMap.computeIfAbsent(hashCode()) {
                Family(all, one, exclude)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Builder

            if (all != other.all) return false
            if (one != other.one) return false
            if (exclude != other.exclude) return false

            return true
        }

        override fun hashCode(): Int {
            var result = all.hashCode()
            result = 31 * result + one.hashCode()
            result = 31 * result + exclude.hashCode()
            return result
        }

    }

}
