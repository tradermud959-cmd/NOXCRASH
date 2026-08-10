sed -i '/enum class AIType/i \
data class AIConfig(val type: AIType, val name: String, val durationDays: Long, val price: java.math.BigDecimal, val priceString: String)\
\
object AIEconomy {\
    val scout = AIConfig(AIType.SCOUT, "AI Scout", 3, java.math.BigDecimal("12000"), "12.000")\
    val smart = AIConfig(AIType.SMART, "AI Smart", 7, java.math.BigDecimal("20000"), "20.000")\
    val pro = AIConfig(AIType.PRO, "AI Pro", 14, java.math.BigDecimal("32000"), "32.000")\
    val void = AIConfig(AIType.VOID, "AI Void", 30, java.math.BigDecimal("48000"), "48.000")\
    \
    fun getConfig(type: AIType): AIConfig = when(type) {\
        AIType.SCOUT -> scout\
        AIType.SMART -> smart\
        AIType.PRO -> pro\
        AIType.VOID -> void\
    }\
}' app/src/main/java/com/example/AIManager.kt
