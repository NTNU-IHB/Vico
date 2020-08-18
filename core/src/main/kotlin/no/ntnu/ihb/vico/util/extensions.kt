package no.ntnu.ihb.vico.util

import java.util.*

fun StringArray(size: Int) = Array(size) { "" }

fun stringArrayOf(vararg str: String) = arrayOf(*str)

fun String.insert(index: Int, str: String): String {
    return this.substring(0, index + 1) + str + this.substring(index + 1)
}

/*fun String.extractEntityAndPropertyName(): PropertyIdentifier {
    val i = indexOf('.')
    if (i == -1) throw IllegalArgumentException()
    val entityName = substring(0, i)
    val propertyName = substring(i + 1)
    return PropertyIdentifier(entityName, propertyName)
}*/

fun Double.formatForOutput(decimalPoints: Int = 4): String {
    return String.format(Locale.US, "%.${decimalPoints}f", this)
}
