package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.PropertyLocator
import no.ntnu.ihb.vico.util.formatForOutput

interface ValueProvider {
    fun get(engine: Engine): Double
}


class TimeProvider : ValueProvider {

    override fun get(engine: Engine): Double {
        return engine.currentTime.formatForOutput(2).toDouble()
    }

}

class ConstantProvider(
    val value: Double
) : ValueProvider {

    override fun get(engine: Engine): Double {
        return value.formatForOutput(2).toDouble()
    }

}


class RealProvider private constructor(
    val name: String,
    private val mod: List<((Pair<Engine, Double>) -> Double)>
) : ValueProvider {

    constructor(name: String) : this(name, listOf())

    operator fun times(value: Number): RealProvider {
        return RealProvider(name, mod + listOf { it.second * value.toDouble() })
    }

    operator fun times(value: RealProvider): RealProvider {
        return RealProvider(name, mod + listOf { it.second * value.get(it.first) })
    }

    operator fun div(value: Number): RealProvider {
        return RealProvider(name, mod + listOf { it.second / value.toDouble() })
    }

    operator fun div(value: RealProvider): RealProvider {
        return RealProvider(name, mod + listOf { it.second / value.get(it.first) })
    }

    operator fun plus(value: Number): RealProvider {
        return RealProvider(name, mod + listOf { it.second + value.toDouble() })
    }

    operator fun plus(value: RealProvider): RealProvider {
        return RealProvider(name, mod + listOf { it.second + value.get(it.first) })
    }

    operator fun minus(value: Number): RealProvider {
        return RealProvider(name, mod + listOf { it.second - value.toDouble() })
    }

    operator fun minus(value: RealProvider): RealProvider {
        return RealProvider(name, mod + listOf { it.second - value.get(it.first) })
    }

    override fun get(engine: Engine): Double {
        val pl = PropertyLocator(engine)
        var value = pl.getRealProperty(name).read()

        mod.forEach {
            value = it.invoke(engine to value)

        }

        return value

    }

    override fun toString(): String {
        return "RealContext(name='$name', mod=$mod)"
    }

}
