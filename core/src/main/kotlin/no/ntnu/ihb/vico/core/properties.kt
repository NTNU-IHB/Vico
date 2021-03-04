package no.ntnu.ihb.vico.core

interface PropertyAccessor : Iterable<Property> {

    val ints: Collection<IntProperty>
    val reals: Collection<RealProperty>
    val bools: Collection<BoolProperty>
    val strs: Collection<StrProperty>

    fun getProperty(name: String): Property
    fun getPropertyOrNull(name: String): Property?
    fun getIntegerProperty(name: String): IntProperty
    fun getIntegerPropertyOrNull(name: String): IntProperty?
    fun getRealProperty(name: String): RealProperty
    fun getRealPropertyOrNull(name: String): RealProperty?
    fun getStringProperty(name: String): StrProperty
    fun getStringPropertyOrNull(name: String): StrProperty?
    fun getBooleanProperty(name: String): BoolProperty
    fun getBooleanPropertyOrNull(name: String): BoolProperty?

}

open class Properties : PropertyAccessor, Iterable<Property> {

    override val ints: MutableSet<IntProperty> = mutableSetOf()
    override val reals: MutableSet<RealProperty> = mutableSetOf()
    override val bools: MutableSet<BoolProperty> = mutableSetOf()
    override val strs: MutableSet<StrProperty> = mutableSetOf()

    fun registerProperties(variables: List<Property>) {

        fun registerProperty(variable: Property) = apply {
            when (variable) {
                is IntProperty -> ints.add(variable)
                is RealProperty -> reals.add(variable)
                is BoolProperty -> bools.add(variable)
                is StrProperty -> strs.add(variable)
            }
        }

        variables.forEach {
            registerProperty(it)
        }
    }

    fun registerProperties(variable: Property, vararg variables: Property) {
        registerProperties(listOf(variable, *variables))
    }

    fun remove(properties: List<Property>) {
        properties.forEach {
            when (it) {
                is IntProperty -> ints.remove(it)
                is RealProperty -> reals.remove(it)
                is StrProperty -> strs.remove(it)
                is BoolProperty -> bools.remove(it)
            }
        }
    }

    fun getAllProperties(): Collection<Property> {
        return ints + reals + strs + bools
    }

    override fun getPropertyOrNull(name: String): Property? {
        return getAllProperties().find { it.name == name }
    }

    override fun getProperty(name: String): Property {
        return getPropertyOrNull(name)
            ?: throw NoSuchElementException(
                "No property named '$name' could be located! " +
                        "Currently registered properties are ${getAllProperties()}."
            )
    }

    override fun getIntegerPropertyOrNull(name: String): IntProperty? {
        return ints.find { it.name == name }
    }

    override fun getIntegerProperty(name: String): IntProperty {
        return getIntegerPropertyOrNull(name)
            ?: throw NoSuchElementException(
                "No property named '$name' could be located! " +
                        "Currently registered Integer properties are ${getAllProperties()}."
            )
    }

    override fun getRealPropertyOrNull(name: String): RealProperty? {
        return reals.find { it.name == name }
    }

    override fun getRealProperty(name: String): RealProperty {
        return getRealPropertyOrNull(name)
            ?: throw NoSuchElementException(
                "No property named '$name' could be located! " +
                        "Currently registered Real properties are ${getAllProperties()}."
            )
    }

    override fun getStringPropertyOrNull(name: String): StrProperty? {
        return strs.find { it.name == name }
    }

    override fun getStringProperty(name: String): StrProperty {
        return getStringPropertyOrNull(name)
            ?: throw NoSuchElementException(
                "No property named '$name' could be located! " +
                        "Currently registered String properties are ${getAllProperties()}."
            )
    }

    override fun getBooleanPropertyOrNull(name: String): BoolProperty? {
        return bools.find { it.name == name }
    }

    override fun getBooleanProperty(name: String): BoolProperty {
        return getBooleanPropertyOrNull(name)
            ?: throw NoSuchElementException(
                "No property named '$name' could be located! " +
                        "Currently registered Boolean properties are ${getAllProperties()}."
            )
    }

    override fun iterator(): Iterator<Property> {
        return (ints + reals + strs + bools).iterator()
    }

    companion object {
        const val PROPERTIES_CHANGED = "propertiesChanged"
    }

}
