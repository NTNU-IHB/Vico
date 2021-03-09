package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.PropertyLocator

fun charts(ctx: ChartsContext.() -> Unit): List<ChartConfig> {
    return ChartsContext().apply(ctx).charts
}

class ChartsContext {
    val charts = mutableListOf<ChartConfig>()

    fun xyChart(title: String, ctx: XYChartContext.() -> Unit) {
        XYChartContext(title).apply(ctx).also {

        }
    }

}

class XYChartContext(
    val title: String
) {

    var width: Int = 800
    var height: Int = 600
    var xLabel: String? = null
    var yLabel: String? = null
    var live: Boolean = false
    var decimationFactor: Int = 1

    fun series(ctx: SeriesContext.() -> Unit) {
        SeriesContext().apply(ctx)
    }

}

class SeriesContext {

    fun x(ctx: VariableContext.() -> Unit) {
        VariableContext().apply(ctx)
    }

    fun y(ctx: VariableContext.() -> Unit) {
        VariableContext().apply(ctx)
    }

}

class VariableContext {

    fun real(name: String) = RealContext(name)

}

class RealContext(
    val name: String,
    private val mod: ((Double) -> Double)? = null
) {

    lateinit var engine: Engine

    operator fun times(value: Double): RealContext {
        return RealContext(name) {
            it * value
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
        return mod?.invoke(value) ?: value
    }

}

sealed class ChartConfig(
    val title: String,
    val xLabel: String,
    val yLabel: String,
) {

    open var width = 800
    open var height = 600


}

class XYChartConfig(
    title: String,
    xLabel: String,
    yLabel: String,
) : ChartConfig(title, xLabel, yLabel)


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
