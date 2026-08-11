package com.example

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun BigDecimal.toNXFormat(): String {
    return this.setScale(15, RoundingMode.DOWN).toPlainString() + " NX"
}

fun Double.toNXFormat(): String {
    return BigDecimal(this.toString()).setScale(15, RoundingMode.DOWN).toPlainString() + " NX"
}

fun BigDecimal.toShortNXFormat(): String {
    val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))
    return formatter.format(this) + " NX"
}

fun Double.toShortNXFormat(): String {
    val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))
    return formatter.format(this) + " NX"
}
