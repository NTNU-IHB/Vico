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

data class PropertyIdentifier(
    val entityName: String,
    val propertyName: String
) {

    fun getProperty(engine: Engine): Property {
        val entity = engine.getEntityByName(entityName)
        return entity.getProperty(propertyName)
    }

    fun getIntegerProperty(engine: Engine): IntProperty {
        val entity = engine.getEntityByName(entityName)
        return entity.getIntegerProperty(propertyName)
    }

    fun getRealProperty(engine: Engine): RealProperty {
        val entity = engine.getEntityByName(entityName)
        return entity.getRealProperty(propertyName)
    }

    fun getStringProperty(engine: Engine): StrProperty {
        val entity = engine.getEntityByName(entityName)
        return entity.getStringProperty(propertyName)
    }

    fun getBooleanProperty(engine: Engine): BoolProperty {
        val entity = engine.getEntityByName(entityName)
        return entity.getBooleanProperty(propertyName)
    }

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