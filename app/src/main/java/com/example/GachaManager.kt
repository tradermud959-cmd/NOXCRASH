package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import kotlin.random.Random

enum class GachaResultType {
    ZONK, CONSOLATION, MINER_LOW, MINER_MID, MINER_HIGH
}

data class GachaResult(
    val type: GachaResultType,
    val minerId: String? = null,
    val rewardAmount: BigDecimal? = null
)

object GachaManager {
    private var prefs: SharedPreferences? = null
    
    private val _lastGachaTime = MutableStateFlow<Long>(0)
    val lastGachaTime: StateFlow<Long> = _lastGachaTime.asStateFlow()
    
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("gacha_prefs", Context.MODE_PRIVATE)
            _lastGachaTime.value = prefs?.getLong("last_gacha_time", 0L) ?: 0L
        }
    }
    
    fun canPullGacha(): Boolean {
        val currentTime = System.currentTimeMillis()
        val timeSinceLast = currentTime - _lastGachaTime.value
        return timeSinceLast >= 24 * 60 * 60 * 1000L
    }
    
    fun getRemainingTimeMs(): Long {
        if (canPullGacha()) return 0L
        val currentTime = System.currentTimeMillis()
        return (24 * 60 * 60 * 1000L) - (currentTime - _lastGachaTime.value)
    }
    
    fun pullGacha(): GachaResult {
        if (!canPullGacha()) return GachaResult(GachaResultType.ZONK)
        
        val currentTime = System.currentTimeMillis()
        prefs?.edit()?.putLong("last_gacha_time", currentTime)?.apply()
        _lastGachaTime.value = currentTime
        
        val roll = Random.nextInt(1, 101)
        
        return when {
            roll == 1 -> GachaResult(GachaResultType.MINER_HIGH, minerId = "ultra_miner") // 1%
            roll in 2..3 -> GachaResult(GachaResultType.MINER_MID, minerId = "fast_miner") // 2%
            roll in 4..15 -> GachaResult(GachaResultType.MINER_LOW, minerId = "slow_miner") // 12%
            roll in 16..40 -> GachaResult(GachaResultType.CONSOLATION, rewardAmount = BigDecimal((10..50).random())) // 25%
            else -> GachaResult(GachaResultType.ZONK) // 60%
        }
    }
}
