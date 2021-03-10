package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.PropertyLocator

fun charts(ctx: ChartsContext.() -> Unit): List<ChartConfig> {
    return ChartsContext().apply(ctx)
}


sealed class ChartConfig(
    val title: String
) {

    var width: Int = 800
    var height: Int = 600
    var xLabel: String? = null
    var yLabel: String? = null
    var live: Boolean = false
    var decimationFactor: Int = 1

}


class ChartsContext : ArrayList<ChartConfig>() {

    fun xyChart(title: String, ctx: XYChartContext.() -> Unit) {
        XYChartContext(title).apply(ctx).also {
            add(it)
        }
    }

}


class XYChartContext(
    title: String
) : ChartConfig(title) {

    internal val series = mutableListOf<SeriesContext>()

    fun series(ctx: SeriesContext.() -> Unit) {
        SeriesContext().apply(ctx).also {
            series.add(it)
        }
    }

}

class SeriesContext {

    lateinit var xGetter: VariableContext
    lateinit var yGetter: VariableContext

    fun x(ctx: VariableContext.() -> Unit) {
        VariableContext().apply(ctx).also {
            xGetter = it
        }
    }

    fun y(ctx: VariableContext.() -> Unit) {
        VariableContext().apply(ctx).also {
            yGetter = it
        }
    }

}

class VariableContext {

    lateinit var variable: RealContext

    fun real(name: String): RealContext {
        return RealContext(name).also {
            this.variable = it
        }
    }

    internal fun get(engine: Engine): Double = variable.get(engine)

}

class RealContext(
    val name: String,
    private val mod: ((Double) -> Double)? = null
) {

    lateinit var engine: Engine

    operator fun times(value: Number): RealContext {
        return RealContext(name) {
            it * value.toDouble()
        }
    }

    operator fun times(value: RealContext): RealContext {
        return RealContext(name) {
            it * value.get(value.engine)
        }
    }

    operator fun div(value: Double): RealContext {
        return RealContext(name) {
            it / value
        }
    }

    operator fun plus(value: Double): RealContext {
        return RealContext(name) {
            it + value
        }
    }

    operator fun minus(value: Double): RealContext {
        return RealContext(name) {
            it - value
        }
    }

    fun get(engine: Engine): Double {
        this.engine = engine
        val pl = PropertyLocator(engine)
        val value = pl.getRealProperty(name).read()
        println(value)
        return mod?.invoke(value) ?: value
    }

}


fun main() {

    charts {

        xyChart("Title") {

            series {

                x {
                    real("dsad.asd") * 10.0 * real("lol")
                }

                y {

                }

            }

        }

    }

}
