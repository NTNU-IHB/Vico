@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.3.5")

import no.ntnu.ihb.vico.dsl.charts

charts {

    timeSeries("Power consumption") {

        live = true
        yLabel = "Power[kW]"

        series("gunnerus") {

            data {
                (realRef("gunnerus.portAzimuth.loadFeedback") * 5) +
                        realRef("gunnerus.starboardAzimuth.loadFeedback") * 5
            }

        }

        series("twin") {
            data {
                (realRef("starboardAzimuth.output_torque") * realRef("starboardAzimuth.output_revs") / 9.5488 / 1000) +
                        (realRef("portAzimuth.output_torque") * realRef("portAzimuth.output_revs") / 9.5488 / 1000)
            }
        }

    }

}
