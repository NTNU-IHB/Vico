package no.ntnu.ihb.acco.core

interface PropertyAccessor {

    fun getIntegerVariable(name: String): IntVar?
    fun getRealVariable(name: String): RealVar?
    fun getStringVariable(name: String): StrVar?
    fun getBooleanVariable(name: String): BoolVar?

}

open class Properties : PropertyAccessor {

    private val _ints = mutableSetOf<IntVar>()
    val ints: Collection<IntVar> = _ints
    private val _reals = mutableSetOf<RealVar>()
    val reals: Collection<RealVar> = _reals
    private val _strs = mutableSetOf<StrVar>()
    val strs: Collection<StrVar> = _strs
    private val _bools = mutableSetOf<BoolVar>()
    val bools: Collection<BoolVar> = _bools

    protected fun registerVariable(variable: Var<*>) = apply {
        when (variable) {
            is IntVar -> _ints.add(variable)
            is RealVar -> _reals.add(variable)
            is StrVar -> _strs.add(variable)
            is BoolVar -> _bools.add(variable)
        }
    }

    protected fun registerVariables(variables: List<Var<*>>) {
        variables.forEach {
            registerVariable(it)
        }
    }

    protected fun registerVariables(vararg variables: Var<*>) {
        variables.forEach {
            registerVariable(it)
        }
    }

    fun remove(variables: List<Var<*>>) {
        variables.forEach {
            when (it) {
                is IntVar -> _ints.remove(it)
                is RealVar -> _reals.remove(it)
                is StrVar -> _strs.remove(it)
                is BoolVar -> _bools.remove(it)
            }
        }
    }

    override fun getIntegerVariable(name: String): IntVar? {
        return _ints.find { it.name == name }
    }

    override fun getRealVariable(name: String): RealVar? {
        return _reals.find { it.name == name }
    }

    override fun getStringVariable(name: String): StrVar? {
        return _strs.find { it.name == name }
    }

    override fun getBooleanVariable(name: String): BoolVar? {
        return _bools.find { it.name == name }
    }

    companion object {
        const val PROPERTIES_CHANGED = "propertiesChanged"
    }

}
