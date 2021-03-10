package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.Properties
import no.ntnu.ihb.vico.core.RealLambdaProperty
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private class DataComponent(
    val data: Double
) : Component {

    override val properties = Properties()

    init {
        properties.registerProperties(
            RealLambdaProperty("data", 1, { it[0] = data })
        )
    }

}


class ChartConfigTest {

    @Test
    fun testRealContext() {

        val d1 = DataComponent(3.0)
        val d2 = DataComponent(2.0)

        Engine().use { engine ->

            engine.createEntity("e1", d1)
            engine.createEntity("e2", d2)

            val r1 = RealProvider("e1.data")
            val r2 = RealProvider("e2.data")

            Assertions.assertEquals(d1.data, r1.get(engine))
            Assertions.assertEquals(d2.data, r2.get(engine))

            val r1mul5 = r1 * 5
            Assertions.assertEquals(d1.data * 5, r1mul5.get(engine))
            val r12 = r1 * r2
            Assertions.assertEquals(d1.data * d2.data, r12.get(engine))
            val r1mulr2mulr1 = r1 * r2 * r1
            Assertions.assertEquals(d1.data * d2.data * d1.data, r1mulr2mulr1.get(engine))
            val r1mulr2mul5 = r1 * r2 * 5
            Assertions.assertEquals(d1.data * d2.data * 5, r1mulr2mul5.get(engine))

        }

    }

    @Test
    fun testChartDSL() {

        Engine().use { engine ->

            engine.createEntity("e1", DataComponent(3.0))
            engine.createEntity("e2", DataComponent(2.0))

            charts {

                xyChart("title") {

                    series("") {

                        x {
                            realRef("e1.data") * realRef("e2.data") * 5
                        }
                        y {
                            realRef("e1.data") * realRef("e2.data")
                        }

                    }

                }

            }.also { charts ->

                Assertions.assertEquals(1, charts.size)

                val c = charts.first() as XYChartContext
                Assertions.assertEquals(30.0, c.series[0].xGetter.get(engine))
                Assertions.assertEquals(6.0, c.series[0].yGetter.get(engine))

            }

        }

    }

}
