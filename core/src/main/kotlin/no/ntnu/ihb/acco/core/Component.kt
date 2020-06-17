package no.ntnu.ihb.acco.core


typealias ComponentClass = Class<out Component>

abstract class Component : EventDispatcher by EventDispatcherImpl() {

    val ints: MutableMap<String, IntVar> = mutableMapOf()
    val reals: MutableMap<String, RealVar> = mutableMapOf()
    val strs: MutableMap<String, StrVar> = mutableMapOf()
    val bools: MutableMap<String, BoolVar> = mutableMapOf()

    protected fun registerVariable(name: String, variable: Var<*>) = apply {
        when (variable) {
            is IntVar -> ints[name] = variable
            is RealVar -> reals[name] = variable
            is StrVar -> strs[name] = variable
            is BoolVar -> bools[name] = variable
        }
    }

    protected fun registerVariables(variables: Map<String, Var<*>>) {
        variables.forEach {
            registerVariable(it.key, it.value)
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

    fun getIntegerVariable(name: String): IntVar {
        return ints[name] ?: throw IllegalStateException("No variable named '$name' of type Int registered!")
    }

    fun getRealVariable(name: String): RealVar {
        return reals[name] ?: throw IllegalStateException("No variable named '$name' of type Real registered!")
    }

    fun getStringVariable(name: String): StrVar {
        return strs[name] ?: throw IllegalStateException("No variable named '$name' of type String registered!")
    }

    fun getBooleanVariable(name: String): BoolVar {
        return bools[name] ?: throw IllegalStateException("No variable named '$name' of type Boolean registered!")
    }

}
