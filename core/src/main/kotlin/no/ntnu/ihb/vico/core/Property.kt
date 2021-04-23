package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.util.StringArray
import no.ntnu.ihb.vico.util.stringArrayOf


enum class Causality {
    LOCAL, INPUT, OUTPUT, PARAMETER, CALCULATED_PARAMETER, UNKNOWN
}

fun interface ReferenceProvider<E> {
    fun invoke(values: E)
}

fun interface Getter<E> {
    fun get(): E
}

fun interface Setter<E> {
    fun set(value: E)
}

enum class PropertyType {
    INT, REAL, STRING, BOOLEAN
}

sealed class Property(
    val component: Component,
    val name: String,
    val size: Int,
    causality: Causality? = null
) {

    val causality: Causality = causality ?: Causality.LOCAL

    val type: PropertyType
        get() = when (this) {
            is IntProperty -> PropertyType.INT
            is RealProperty -> PropertyType.REAL
            is StrProperty -> PropertyType.STRING
            is BoolProperty -> PropertyType.BOOLEAN
        }

    override fun toString(): String {
        return "Property(name='$name', type=$type, causality=$causality, size=$size)"
    }

}

abstract class IntProperty(component: Component, name: String, size: Int, causality: Causality? = null) :
    Property(component, name, size, causality) {
    open fun read(): Int = read(IntArray(size)).first()
    open fun write(value: Int) = write(intArrayOf(value))
    abstract fun read(values: IntArray): IntArray
    abstract fun write(values: IntArray)
}

abstract class RealProperty(component: Component, name: String, size: Int, causality: Causality? = null) :
    Property(component, name, size, causality) {
    open fun read(): Double = read(DoubleArray(size)).first()
    open fun write(value: Double) = write(doubleArrayOf(value))
    abstract fun read(values: DoubleArray): DoubleArray
    abstract fun write(values: DoubleArray)
}

abstract class StrProperty(component: Component, name: String, size: Int, causality: Causality? = null) :
    Property(component, name, size, causality) {
    open fun read(): String = read(StringArray(size)).first()
    open fun write(value: String) = write(stringArrayOf(value))
    abstract fun read(values: StringArray): StringArray
    abstract fun write(values: StringArray)
}

abstract class BoolProperty(component: Component, name: String, size: Int, causality: Causality? = null) :
    Property(component, name, size, causality) {
    open fun read(): Boolean = read(BooleanArray(size)).first()
    open fun write(value: Boolean) = write(booleanArrayOf(value))
    abstract fun read(values: BooleanArray): BooleanArray
    abstract fun write(values: BooleanArray)
}

class IntScalarProperty(
    component: Component,
    name: String,
    private val getter: Getter<Int>,
    private val setter: Setter<Int>? = null,
    causality: Causality? = null
) : IntProperty(component, name, 1, causality) {

    override fun read(): Int {
        return getter.get()
    }

    override fun read(values: IntArray): IntArray {
        require(size == values.size)
        return values.also { it[0] = read() }
    }

    override fun write(value: Int) {
        setter?.set(value) ?: throw IllegalStateException("No setter defined")
    }

    override fun write(values: IntArray) {
        require(size == values.size)
        write(values.first())
    }

}

class IntLambdaProperty(
    component: Component,
    name: String,
    size: Int,
    private val getter: ReferenceProvider<IntArray>,
    private val setter: ReferenceProvider<IntArray>? = null,
    causality: Causality? = null
) : IntProperty(component, name, size, causality) {

    override fun read(values: IntArray): IntArray {
        require(size == values.size)
        getter.invoke(values)
        return values
    }

    override fun write(values: IntArray) {
        require(size == values.size)
        if (setter == null) throw IllegalStateException("No setter defined")
        setter.invoke(values)
    }
}

class RealScalarProperty(
    component: Component,
    name: String,
    private val getter: Getter<Double>,
    private val setter: Setter<Double>? = null,
    causality: Causality? = null
) : RealProperty(component, name, 1, causality) {

    override fun read(): Double {
        return getter.get()
    }

    override fun read(values: DoubleArray): DoubleArray {
        require(size == values.size)
        return values.also { it[0] = read() }
    }

    override fun write(value: Double) {
        setter?.set(value) ?: throw IllegalStateException("No setter defined")
    }

    override fun write(values: DoubleArray) {
        require(size == values.size)
        write(values.first())
    }

}

class RealLambdaProperty(
    component: Component,
    name: String,
    size: Int,
    private val getter: ReferenceProvider<DoubleArray>,
    private val setter: ReferenceProvider<DoubleArray>? = null,
    causality: Causality? = null
) : RealProperty(component, name, size, causality) {

    override fun read(values: DoubleArray): DoubleArray {
        require(size == values.size)
        getter.invoke(values)
        return values
    }

    override fun write(values: DoubleArray) {
        require(size == values.size)
        if (setter == null) throw IllegalStateException("No setter defined")
        setter.invoke(values)
    }

}

class StrLambdaProperty(
    component: Component,
    name: String,
    size: Int,
    private val getter: ReferenceProvider<StringArray>,
    private val setter: ReferenceProvider<StringArray>? = null,
    causality: Causality? = null
) : StrProperty(component, name, size, causality) {

    override fun read(values: StringArray): StringArray {
        require(size == values.size)
        getter.invoke(values)
        return values
    }

    override fun write(values: StringArray) {
        require(size == values.size)
        if (setter == null) throw IllegalStateException("No setter defined")
        setter.invoke(values)
    }

}

class StrScalarProperty(
    component: Component,
    name: String,
    private val getter: Getter<String>,
    private val setter: Setter<String>? = null,
    causality: Causality? = null
) : StrProperty(component, name, 1, causality) {

    override fun read(): String {
        return getter.get()
    }

    override fun read(values: StringArray): StringArray {
        require(size == values.size)
        return values.also { it[0] = read() }
    }

    override fun write(value: String) {
        setter?.set(value) ?: throw IllegalStateException("No setter defined")
    }

    override fun write(values: StringArray) {
        require(size == values.size)
        write(values.first())
    }

}

class BoolLambdaProperty(
    component: Component,
    name: String,
    size: Int,
    private val getter: ReferenceProvider<BooleanArray>,
    private val setter: ReferenceProvider<BooleanArray>? = null,
    causality: Causality? = null
) : BoolProperty(component, name, size, causality) {

    override fun read(values: BooleanArray): BooleanArray {
        require(size == values.size)
        getter.invoke(values)
        return values
    }

    override fun write(values: BooleanArray) {
        require(size == values.size)
        if (setter == null) throw IllegalStateException("No setter defined")
        setter.invoke(values)
    }

}

class BoolScalarProperty(
    component: Component,
    name: String,
    private val getter: Getter<Boolean>,
    private val setter: Setter<Boolean>? = null,
    causality: Causality? = null
) : BoolProperty(component, name, 1, causality) {

    override fun read(): Boolean {
        return getter.get()
    }

    override fun read(values: BooleanArray): BooleanArray {
        require(size == values.size)
        return values.also { it[0] = read() }
    }

    override fun write(value: Boolean) {
        setter?.set(value) ?: throw IllegalStateException("No setter defined")
    }

    override fun write(values: BooleanArray) {
        require(size == values.size)
        write(values.first())
    }

}
