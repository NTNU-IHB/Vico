package no.ntnu.ihb.acco.util

import java.util.*

fun StringArray(size: Int) = no.ntnu.ihb.acco.util.StringArray(size) { "" }

fun Double.formatForOutput(decimalPoints: Int = 4): String {
    return String.format(Locale.US, "%.${decimalPoints}f", this)
}
