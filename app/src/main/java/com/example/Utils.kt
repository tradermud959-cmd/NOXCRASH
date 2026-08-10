package com.example

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toNXFormat(): String {
    return this.setScale(15, RoundingMode.DOWN).toPlainString() + " NX"
}

fun Double.toNXFormat(): String {
    return BigDecimal(this.toString()).setScale(15, RoundingMode.DOWN).toPlainString() + " NX"
}
