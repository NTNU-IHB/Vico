package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.util.StringArray
import no.ntnu.ihb.vico.util.stringArrayOf


enum class Causality {
    LOCAL, INPUT, OUTPUT, PARAMETER, CALCULATED_PARAMETER, UNKNOWN
}

fun interface ReferenceProvider<E> {
    fun invoke(values: E)
}

enum class PropertyType {
    INT, REAL, STRING, BOOLEAN
}

open class UnboundProperty(
    val entityName: String,
    val componentName: String,
    val propertyName: String
) {

    fun bounded(engine: Engine) = BoundProperty(engine, entityName, componentName, propertyName)

    companion object {

        fun parse(identifier: String): UnboundProperty {
            val split = identifier.split(".")
            require(split.size >= 3) { "Illegal identifier format: $identifier" }
            val entityName = split[0]
            val componentName = split[1]
            val propertyName = split.drop(2).joinToString(".")
            return UnboundProperty(
                entityName, componentName, propertyName
            )
        }

    }

}

class BoundProperty(
    val engine: Engine,
    entityName: String,
    componentName: String,
    propertyName: String
) : UnboundProperty(entityName, componentName, propertyName) {

    val entity by lazy { engine.getEntityByName(entityName) }
    val component by lazy { entity.get(componentName) }
    val property by lazy { component.getProperty(propertyName) }

}

sealed class Property(
    val name: String,
    val size: Int
) {

    abstract val causality: Causality

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

abstract class IntProperty(name: String, size: Int) : Property(name, size) {
    fun read(): IntArray = read(IntArray(size))
    fun write(value: Int) = write(intArrayOf(value))
    abstract fun read(values: IntArray): IntArray
    abstract fun write(values: IntArray)
}

abstract class RealProperty(name: String, size: Int) : Property(name, size) {
    fun read(): DoubleArray = read(DoubleArray(size))
    fun write(value: Double) = write(doubleArrayOf(value))
    abstract fun read(values: DoubleArray): DoubleArray
    abstract fun write(values: DoubleArray)
}

abstract class StrProperty(name: String, size: Int) : Property(name, size) {
    fun read(): StringArray = read(StringArray(size))
    fun write(value: String) = write(stringArrayOf(value))
    abstract fun read(values: StringArray): StringArray
    abstract fun write(values: StringArray)
}

abstract class BoolProperty(name: String, size: Int) : Property(name, size) {
    fun read(): BooleanArray = read(BooleanArray(size))
    fun write(value: Boolean) = write(booleanArrayOf(value))
    abstract fun read(values: BooleanArray): BooleanArray
    abstract fun write(values: BooleanArray)
}

class IntLambdaProperty(
    name: String,
    size: Int,
    private val getter: ReferenceProvider<IntArray>,
    private val setter: ReferenceProvider<IntArray>? = null,
    override val causality: Causality = Causality.LOCAL
) : IntProperty(name, size) {

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

class RealLambdaProperty(
    name: String,
    size: Int,
    private val getter: ReferenceProvider<DoubleArray>,
    private val setter: ReferenceProvider<DoubleArray>? = null,
    override val causality: Causality = Causality.LOCAL
) : RealProperty(name, size) {

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
    name: String,
    size: Int,
    private val getter: ReferenceProvider<StringArray>,
    private val setter: ReferenceProvider<StringArray>? = null,
    override val causality: Causality = Causality.LOCAL
) : StrProperty(name, size) {

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

class BoolLambdaProperty(
    name: String,
    size: Int,
    private val getter: ReferenceProvider<BooleanArray>,
    private val setter: ReferenceProvider<BooleanArray>? = null,
    override val causality: Causality = Causality.LOCAL
) : BoolProperty(name, size) {

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
