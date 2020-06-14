package no.ntnu.ihb.acco.core

interface PropertyAccessor {

    fun getIntegerVariable(name: String): IntVar
    fun getRealVariable(name: String): RealVar
    fun getStringVariable(name: String): StrVar
    fun getBooleanVariable(name: String): BoolVar

}

internal class Properties : PropertyAccessor {

    val ints = mutableMapOf<String, IntVar>()
    val reals = mutableMapOf<String, RealVar>()
    val strs = mutableMapOf<String, StrVar>()
    val bools = mutableMapOf<String, BoolVar>()

    fun add(variables: Map<String, Var<*>>) {
        variables.forEach { (name, `var`) ->
            when (`var`) {
                is IntVar -> ints[name] = `var`
                is RealVar -> reals[name] = `var`
                is StrVar -> strs[name] = `var`
                is BoolVar -> bools[name] = `var`
            }
        }
    }

    fun remove(variables: Map<String, Var<*>>) {
        variables.forEach { (name, `var`) ->
            when (`var`) {
                is IntVar -> ints.remove(name)
                is RealVar -> reals.remove(name)
                is StrVar -> strs.remove(name)
                is BoolVar -> bools.remove(name)
            }
        }
    }

    override fun getIntegerVariable(name: String): IntVar {
        return ints[name] ?: throw IllegalStateException("No variable named '$name' of type Int registered!")
    }

    override fun getRealVariable(name: String): RealVar {
        return reals[name] ?: throw IllegalStateException("No variable named '$name' of type Real registered!")
    }

    override fun getStringVariable(name: String): StrVar {
        return strs[name] ?: throw IllegalStateException("No variable named '$name' of type String registered!")
    }

    override fun getBooleanVariable(name: String): BoolVar {
        return bools[name] ?: throw IllegalStateException("No variable named '$name' of type Boolean registered!")
    }

}
