package no.ntnu.ihb.acco.core


typealias ComponentClass = Class<out Component>

abstract class Component {

    private val ints = mutableMapOf<String, IntVar>()
    private val reals = mutableMapOf<String, RealVar>()
    private val strs = mutableMapOf<String, StrVar>()
    private val bools = mutableMapOf<String, BoolVar>()

    init {
        /* javaClass.fields.forEach { field ->
             if (field.isAnnotationPresent(Property::class.java)) {

             }
         }*/
    }

    fun getVariable(name: String): Var<*> {
        return (ints + reals + strs + bools)[name] ?: throw IllegalArgumentException()
    }

    protected fun registerVariable(name: String, `var`: Var<*>) {
        when (`var`) {
            is IntVar -> ints[name] = `var`
            is RealVar -> reals[name] = `var`
            is StrVar -> strs[name] = `var`
            is BoolVar -> bools[name] = `var`
        }
    }

    fun readInteger(name: String, ref: IntArray) {
        ints[name]?.read(ref) ?: throw IllegalStateException("No variable named '$name' of type Int registered!")
    }

    fun readReal(name: String, ref: DoubleArray) {
        reals[name]?.read(ref) ?: throw IllegalStateException("No variable named '$name' of type Real registered!")
    }

    fun readString(name: String, ref: StringArray) {
        strs[name]?.read(ref) ?: throw IllegalStateException("No variable named '$name' of type String registered!")
    }

    fun readBoolean(name: String, ref: BooleanArray) {
        bools[name]?.read(ref) ?: throw IllegalStateException("No variable named '$name' of type Boolean registered!")
    }

    fun writeInteger(name: String, ref: IntArray) {
        ints[name]?.read(ref) ?: throw IllegalStateException("No variable named '$name' of type Int registered!")
    }

    fun writeReal(name: String, values: DoubleArray) {
        reals[name]?.write(values) ?: throw IllegalStateException("No variable named '$name' of type Real registered!")
    }

    fun writeString(name: String, values: StringArray) {
        strs[name]?.write(values) ?: throw IllegalStateException("No variable named '$name' of type String registered!")
    }

    fun writeBoolean(name: String, values: BooleanArray) {
        bools[name]?.write(values)
            ?: throw IllegalStateException("No variable named '$name' of type Boolean registered!")
    }

}
