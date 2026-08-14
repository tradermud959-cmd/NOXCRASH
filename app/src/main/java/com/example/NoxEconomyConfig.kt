package com.example

import java.math.BigDecimal
import java.math.RoundingMode

object NoxEconomyConfig {
    val MAX_SUPPLY = BigDecimal("50000000")
    
    // 150 years in seconds = 150 * 365.25 * 24 * 3600 = 4733640000
    val REFERENCE_HORIZON_SECONDS = BigDecimal("4733640000")
    
    // NOX_DIFFICULTY is chosen such that REWARD_RATE = HASHRATE / NOX_DIFFICULTY
    // To make 50 MHX/s (50,000,000 HX/s) produce ~2000 NX in a reasonable time,
    // and overall economy stretch for 150 years if hashrate is balanced.
    // If NOX_DIFFICULTY = 4733640000, 1 HX/S produces 1/4733640000 NX/sec.
    val NOX_DIFFICULTY = BigDecimal("4733640000")
    
    fun getHashrateForMiner(minerId: String): BigDecimal {
        return when (minerId) {
            "free_miner" -> BigDecimal("1000") // 1 KHX/S
            "basic_miner" -> BigDecimal("10000") // 10 KHX/S
            "slow_miner" -> BigDecimal("100000") // 100 KHX/S
            "fast_miner" -> BigDecimal("1000000") // 1 MHX/S
            "ultra_miner" -> BigDecimal("10000000") // 10 MHX/S
            "void_miner" -> BigDecimal("50000000") // 50 MHX/S
            else -> BigDecimal("1000")
        }
    }
    
    fun getHashrateString(hashrate: BigDecimal): String {
        val hx = hashrate.toDouble()
        return when {
            hx >= 1_000_000_000_000_000_000.0 -> String.format(java.util.Locale.US, "%.2f EHX/S", hx / 1_000_000_000_000_000_000.0)
            hx >= 1_000_000_000_000_000.0 -> String.format(java.util.Locale.US, "%.2f PHX/S", hx / 1_000_000_000_000_000.0)
            hx >= 1_000_000_000_000.0 -> String.format(java.util.Locale.US, "%.2f THX/S", hx / 1_000_000_000_000.0)
            hx >= 1_000_000_000.0 -> String.format(java.util.Locale.US, "%.2f GHX/S", hx / 1_000_000_000.0)
            hx >= 1_000_000.0 -> String.format(java.util.Locale.US, "%.0f MHX/S", hx / 1_000_000.0)
            hx >= 1_000.0 -> String.format(java.util.Locale.US, "%.0f KHX/S", hx / 1_000.0)
            else -> String.format(java.util.Locale.US, "%.0f HX/S", hx)
        }
    }
    
    fun getTargetRewardForMiner(minerId: String): BigDecimal {
        return when (minerId) {
            "free_miner" -> BigDecimal("2")
            "basic_miner" -> BigDecimal("5")
            "slow_miner" -> BigDecimal("25")
            "fast_miner" -> BigDecimal("120")
            "ultra_miner" -> BigDecimal("550")
            "void_miner" -> BigDecimal("2000")
            else -> BigDecimal("0")
        }
    }

    fun getRewardRatePerSecond(hashrate: BigDecimal): BigDecimal {
        return hashrate.divide(NOX_DIFFICULTY, 15, RoundingMode.DOWN)
    }

    fun getMinerName(minerId: String): String {
        return when (minerId) {
            "free_miner" -> "FREE MINER"
            "basic_miner" -> "BASIC MINER"
            "slow_miner" -> "SLOW MINER"
            "fast_miner" -> "FAST MINER"
            "ultra_miner" -> "ULTRA MINER"
            "void_miner" -> "VOID MINER"
            else -> "UNKNOWN MINER"
        }
    }
    
    fun getMinerPrice(minerId: String): BigDecimal {
        return when (minerId) {
            "free_miner" -> BigDecimal("0")
            "basic_miner" -> BigDecimal("25")
            "slow_miner" -> BigDecimal("100")
            "fast_miner" -> BigDecimal("400")
            "ultra_miner" -> BigDecimal("1500")
            "void_miner" -> BigDecimal("4000")
            else -> BigDecimal("0")
        }
    }
}
