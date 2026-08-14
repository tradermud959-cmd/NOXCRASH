package com.example

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun BigDecimal.toNXFormat(): String {
    return this.toPlainString() + " NX"
}

fun Double.toNXFormat(): String {
    return BigDecimal(this.toString()).toPlainString() + " NX"
}

fun BigDecimal.toShortNXFormat(): String {
    val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale("id", "ID")))
    return formatter.format(this) + " NX"
}

fun Double.toShortNXFormat(): String {
    val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale("id", "ID")))
    return formatter.format(this) + " NX"
}

fun BigDecimal.toMiningProgressFormat(): String {
    return this.setScale(15, RoundingMode.DOWN).toPlainString() + " NX"
}

fun Double.toMiningProgressFormat(): String {
    return BigDecimal(this.toString()).setScale(15, RoundingMode.DOWN).toPlainString() + " NX"
}

fun String.formatNXDisplay(): String {
    try {
        val cleanString = this.replace(" NX", "")
        val dec = BigDecimal(cleanString)
        val plain = dec.setScale(15, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
        return (if (plain.contains(".")) plain else "$plain.0") + " NX"
    } catch (e: Exception) {
        return this
    }
}
