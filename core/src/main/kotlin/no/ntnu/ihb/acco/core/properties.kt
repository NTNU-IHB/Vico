package no.ntnu.ihb.acco.core

interface PropertyAccessor {

    fun getProperty(name: String): Property?
    fun getIntegerProperty(name: String): IntProperty?
    fun getRealProperty(name: String): RealProperty?
    fun getStringProperty(name: String): StrProperty?
    fun getBooleanProperty(name: String): BoolProperty?

}

open class Properties : PropertyAccessor {

    private val _ints: MutableSet<IntProperty> = mutableSetOf()
    val ints: Collection<IntProperty> = _ints
    private val _reals: MutableSet<RealProperty> = mutableSetOf()
    val reals: Collection<RealProperty> = _reals
    private val _strs: MutableSet<StrProperty> = mutableSetOf()
    val strs: Collection<StrProperty> = _strs
    private val _bools: MutableSet<BoolProperty> = mutableSetOf()
    val bools: Collection<BoolProperty> = _bools

    protected fun registerProperties(variables: List<Property>) {

        fun registerProperty(variable: Property) = apply {
            when (variable) {
                is IntProperty -> _ints.add(variable)
                is RealProperty -> _reals.add(variable)
                is StrProperty -> _strs.add(variable)
                is BoolProperty -> _bools.add(variable)
            }
        }

        variables.forEach {
            registerProperty(it)
        }
    }

    protected fun registerProperties(variable: Property, vararg variables: Property) {
        registerProperties(listOf(variable, *variables))
    }

    fun remove(properties: List<Property>) {
        properties.forEach {
            when (it) {
                is IntProperty -> _ints.remove(it)
                is RealProperty -> _reals.remove(it)
                is StrProperty -> _strs.remove(it)
                is BoolProperty -> _bools.remove(it)
            }
        }
    }

    fun getProperties(): Collection<Property> {
        return _ints + _reals + _strs + _bools
    }

    override fun getProperty(name: String): Property? {
        return getProperties().find { it.name == name }
    }

    override fun getIntegerProperty(name: String): IntProperty? {
        return _ints.find { it.name == name }
    }

    override fun getRealProperty(name: String): RealProperty? {
        return _reals.find { it.name == name }
    }

    override fun getStringProperty(name: String): StrProperty? {
        return _strs.find { it.name == name }
    }

    override fun getBooleanProperty(name: String): BoolProperty? {
        return _bools.find { it.name == name }
    }

    companion object {
        const val PROPERTIES_CHANGED = "propertiesChanged"
    }

}
