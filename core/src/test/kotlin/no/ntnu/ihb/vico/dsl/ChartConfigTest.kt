package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.Properties
import no.ntnu.ihb.vico.core.RealLambdaProperty
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private class DataComponent(
    private val data: Double
) : Component {

    override val properties = Properties()

    init {
        properties.registerProperties(
            RealLambdaProperty("data", 1, { data })
        )
    }

}


class ChartConfigTest {

    @Test
    fun testChartDSL() {

        Engine().use { engine ->

            engine.createEntity("e1", DataComponent(3.0))
            engine.createEntity("e2", DataComponent(2.0))

            // println((engine.getEntityByName("e1").properties.first() as RealProperty).read(DoubleArray(1)).toList())

            val charts = charts {

                xyChart("title") {

                    series {

                        x {
                            real("e1.data") * real("e2.data") * 5
                        }

                        y {
                            real("e1.data") * real("e2.data") * 5
                        }

                    }

                }

            }

            val c = charts[0] as XYChartContext
            Assertions.assertEquals(25.0, c.series[0].xGetter.get(engine))

        }

    }

}
