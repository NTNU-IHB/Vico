package no.ntnu.ihb.vico.dsl


fun charts(ctx: ChartsContext.() -> Unit): List<ChartConfig> {
    return ChartsContext().apply(ctx)
}

sealed class ChartConfig(
    val title: String
) {

    var width: Int = 800
    var height: Int = 600
    var live: Boolean = false
    var decimationFactor: Int = 1

    abstract val xLabel: String
    abstract val yLabel: String

    val series = mutableListOf<AbstractSeriesContext>()

}


@Scoped
class ChartsContext : ArrayList<ChartConfig>() {

    fun xyChart(title: String, ctx: XYChartContext.() -> Unit) {
        XYChartContext(title).apply(ctx).also {
            add(it)
        }
    }

    fun timeSeries(title: String = "Title", ctx: TimeSeriesChartContext.() -> Unit) {
        TimeSeriesChartContext(title).apply(ctx).also {
            add(it)
        }
    }

}

class XYChartContext(
    title: String
) : ChartConfig(title) {

    override var xLabel: String = ""
    override val yLabel: String = ""

    fun series(name: String = "Title", ctx: XYSeriesContext.() -> Unit) {
        XYSeriesContext(name).apply(ctx).also {
            series.add(it)
        }
    }

}

class TimeSeriesChartContext(
    title: String
) : ChartConfig(title) {

    override val xLabel: String = "Time[s]"
    override var yLabel: String = ""

    fun series(name: String, ctx: TimeSeriesContext.() -> Unit) {
        TimeSeriesContext(name).apply(ctx).also {
            series.add(it)
        }
    }

}

sealed class AbstractSeriesContext(
    val name: String
) {

    open lateinit var xGetter: ValueProvider
    lateinit var yGetter: ValueProvider

}

@Scoped
class XYSeriesContext(
    name: String
) : AbstractSeriesContext(name) {

    fun x(ctx: DataContext.() -> ValueProvider) {
        xGetter = DataContext.let(ctx)
    }

    fun y(ctx: DataContext.() -> ValueProvider) {
        yGetter = DataContext.let(ctx)
    }

}

@Scoped
class TimeSeriesContext(
    name: String
) : AbstractSeriesContext(name) {

    override var xGetter: ValueProvider = TimeProvider()

    fun data(ctx: DataContext.() -> ValueProvider) {
        yGetter = DataContext.let(ctx)
    }

}

@Scoped
object DataContext {

    fun realRef(name: String) = RealProvider(name)
    fun constant(value: Number) = ConstantProvider(value.toDouble())

}



