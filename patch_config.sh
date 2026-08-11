sed -i '/fun getRewardRatePerSecond/i \
    fun getTargetRewardForMiner(minerId: String): BigDecimal {\
        return when (minerId) {\
            "free_miner" -> BigDecimal("2")\
            "basic_miner" -> BigDecimal("5")\
            "slow_miner" -> BigDecimal("25")\
            "fast_miner" -> BigDecimal("120")\
            "ultra_miner" -> BigDecimal("550")\
            "void_miner" -> BigDecimal("2000")\
            else -> BigDecimal("0")\
        }\
    }\
' app/src/main/java/com/example/NoxEconomyConfig.kt
