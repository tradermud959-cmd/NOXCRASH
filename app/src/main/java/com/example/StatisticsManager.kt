package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

object StatisticsManager {
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "noxcrash_stats"
    
    private val KEY_TOTAL_SESSIONS = "total_sessions"
    private val KEY_TOTAL_REWARD = "total_reward"
    private val KEY_TOTAL_PURCHASES = "total_purchases"
    private val KEY_TOTAL_MINING_TIME = "total_mining_time" // in hours
    
    private val _totalSessions = MutableStateFlow(0)
    val totalSessions: StateFlow<Int> = _totalSessions.asStateFlow()
    
    private val _totalReward = MutableStateFlow(BigDecimal.ZERO)
    val totalReward: StateFlow<BigDecimal> = _totalReward.asStateFlow()
    
    private val _totalPurchases = MutableStateFlow(0)
    val totalPurchases: StateFlow<Int> = _totalPurchases.asStateFlow()
    
    private val _totalMiningTime = MutableStateFlow(0L)
    val totalMiningTime: StateFlow<Long> = _totalMiningTime.asStateFlow()
    
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadStats()
    }
    
    private fun loadStats() {
        _totalSessions.value = prefs.getInt(KEY_TOTAL_SESSIONS, 0)
        
        val rewardString = prefs.getString(KEY_TOTAL_REWARD, "0")
        _totalReward.value = try { BigDecimal(rewardString) } catch (e: Exception) { BigDecimal.ZERO }
        
        _totalPurchases.value = prefs.getInt(KEY_TOTAL_PURCHASES, 0)
        _totalMiningTime.value = prefs.getLong(KEY_TOTAL_MINING_TIME, 0L)
    }
    
    fun addSession(hours: Long) {
        _totalSessions.value += 1
        _totalMiningTime.value += hours
        prefs.edit()
            .putInt(KEY_TOTAL_SESSIONS, _totalSessions.value)
            .putLong(KEY_TOTAL_MINING_TIME, _totalMiningTime.value)
            .apply()
    }
    
    fun addReward(amount: BigDecimal) {
        _totalReward.value = _totalReward.value.add(amount)
        prefs.edit().putString(KEY_TOTAL_REWARD, _totalReward.value.toPlainString()).apply()
    }
    
    fun addPurchase() {
        _totalPurchases.value += 1
        prefs.edit().putInt(KEY_TOTAL_PURCHASES, _totalPurchases.value).apply()
    }


    fun reload() { loadStats() }
}
