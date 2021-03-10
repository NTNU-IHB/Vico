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

    lateinit var xGetter: RealContext
    lateinit var yGetter: RealContext

    fun real(name: String) = RealContext(name)

    fun x(ctx: () -> RealContext) {
        xGetter = ctx.invoke()
    }

    fun y(ctx: () -> RealContext) {
        yGetter = ctx.invoke()
    }

}

class RealContext(
    val name: String,
    private val mod: List<((Pair<Engine, Double>) -> Double)> = mutableListOf()
) {

    operator fun times(value: Number): RealContext {
        return RealContext(name, mod + listOf { it.second * value.toDouble() })
    }

    operator fun times(value: RealContext): RealContext {
        return RealContext(name, mod + listOf { it.second * value.get(it.first) })
    }

    operator fun div(value: Number): RealContext {
        return RealContext(name, mod + listOf { it.second / value.toDouble() })
    }

    operator fun div(value: RealContext): RealContext {
        return RealContext(name, mod + listOf { it.second / value.get(it.first) })
    }

    operator fun plus(value: Number): RealContext {
        return RealContext(name, mod + listOf { it.second + value.toDouble() })
    }

    operator fun plus(value: RealContext): RealContext {
        return RealContext(name, mod + listOf { it.second + value.get(it.first) })
    }

    operator fun minus(value: Number): RealContext {
        return RealContext(name, mod + listOf { it.second - value.toDouble() })
    }

    operator fun minus(value: RealContext): RealContext {
        return RealContext(name, mod + listOf { it.second - value.get(it.first) })
    }

    fun get(engine: Engine): Double {
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


fun main() {

    charts {

        xyChart("Title") {

            series {

                x {
                    real("dsad.asd") * 10.0 * real("lol")
                }

            }

        }

    }

}
