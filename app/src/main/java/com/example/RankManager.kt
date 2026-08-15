package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Random

data class SimulatedBot(
    val name: String,
    val baseBalance: BigDecimal,
    val hourlyRate: BigDecimal
)

object RankManager {
    private var prefs: SharedPreferences? = null
    
    private val _userRank = MutableStateFlow(100)
    val userRank: StateFlow<Int> = _userRank.asStateFlow()
    
    private val _topPlayers = MutableStateFlow<List<RankPlayer>>(emptyList())
    val topPlayers: StateFlow<List<RankPlayer>> = _topPlayers.asStateFlow()

    private var installTimeMs: Long = 0L
    
    private val bots = listOf(
        SimulatedBot("NoxMaster", BigDecimal("500000"), BigDecimal("5000")),
        SimulatedBot("ZeroCool", BigDecimal("250000"), BigDecimal("3000")),
        SimulatedBot("Phantom", BigDecimal("100000"), BigDecimal("1500")),
        SimulatedBot("Cipher", BigDecimal("50000"), BigDecimal("800")),
        SimulatedBot("AcidBurn", BigDecimal("25000"), BigDecimal("400")),
        SimulatedBot("CrashOverride", BigDecimal("10000"), BigDecimal("200")),
        SimulatedBot("Neon", BigDecimal("5000"), BigDecimal("100")),
        SimulatedBot("Ghost", BigDecimal("2500"), BigDecimal("50")),
        SimulatedBot("Glitch", BigDecimal("1000"), BigDecimal("25")),
        SimulatedBot("Byte", BigDecimal("500"), BigDecimal("10")),
        SimulatedBot("ScriptKiddie", BigDecimal("250"), BigDecimal("5")),
        SimulatedBot("Noob", BigDecimal("100"), BigDecimal("2"))
    )

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("rank_prefs", Context.MODE_PRIVATE)
            
            // Set install time to generate time delta
            installTimeMs = prefs!!.getLong("install_time", 0L)
            if (installTimeMs == 0L) {
                // If this is the first time running the RankManager,
                // set the install time to now minus a random 1-3 days to give bots a head start
                installTimeMs = System.currentTimeMillis() - (86400000L * 2) 
                prefs!!.edit().putLong("install_time", installTimeMs).apply()
            }
        }
    }

    fun calculateRank(userBalanceStr: String, username: String) {
        val userBalance = try {
            BigDecimal(userBalanceStr.replace(" NX", ""))
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        
        val currentTimeMs = System.currentTimeMillis()
        val elapsedHours = BigDecimal((currentTimeMs - installTimeMs).toDouble() / 3600000.0)
        
        // Generate pseudo-random fluctuation based on current day so it changes but is consistent
        val currentDay = currentTimeMs / 86400000L
        val rng = Random(currentDay)

        val currentBots = bots.map { bot ->
            // Fluctuates between 80% to 120% of their base speed for the day
            val fluctuation = BigDecimal(0.8 + (rng.nextDouble() * 0.4))
            val earned = bot.hourlyRate.multiply(elapsedHours).multiply(fluctuation)
            var currentBotBalance = bot.baseBalance.add(earned)
            
            // Random jackpot chance for some bots
            if (rng.nextDouble() > 0.95) {
                currentBotBalance = currentBotBalance.add(bot.baseBalance.multiply(BigDecimal("0.5")))
            }

            RankPlayer(bot.name, currentBotBalance, isUser = false)
        }
        
        val userPlayer = RankPlayer(username, userBalance, isUser = true)
        
        // Combine and sort
        val allPlayers = (currentBots + userPlayer).sortedByDescending { it.balance }
        
        _topPlayers.value = allPlayers
        
        // Find user rank
        val rankIndex = allPlayers.indexOfFirst { it.isUser } + 1
        
        if (rankIndex == allPlayers.size) {
            // User is at the very bottom of the explicit list.
            // Calculate a pseudo-rank for lower balances (simulate 9000+ players)
            val lowestBotBalance = currentBots.last().balance
            if (userBalance < lowestBotBalance) {
                val difference = lowestBotBalance.subtract(userBalance)
                // 1 rank per 0.1 NX difference, for example
                val rankOffset = difference.multiply(BigDecimal("10")).toInt().coerceAtLeast(1)
                _userRank.value = rankIndex + rankOffset
            } else {
                _userRank.value = rankIndex
            }
        } else {
            _userRank.value = rankIndex
        }
    }
}
