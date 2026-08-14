package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.util.Calendar

object CheckInManager {
    private var prefs: SharedPreferences? = null
    
    private val _lastCheckInTime = MutableStateFlow(0L)
    private val _currentStreak = MutableStateFlow(0)
    
    val canCheckInToday = MutableStateFlow(false)
    val currentDayToClaim = MutableStateFlow(1)
    
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    val rewards = listOf(
        BigDecimal("5"),
        BigDecimal("15"),
        BigDecimal("30"),
        BigDecimal("50"),
        BigDecimal("100"),
        BigDecimal("250"),
        BigDecimal("500")
    )

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("checkin_prefs", Context.MODE_PRIVATE)
            _lastCheckInTime.value = prefs?.getLong("last_checkin_time", 0L) ?: 0L
            _currentStreak.value = prefs?.getInt("current_streak", 0) ?: 0
            refreshState()
        }
    }

    fun refreshState() {
        val now = System.currentTimeMillis()
        val last = _lastCheckInTime.value
        val streak = _currentStreak.value

        if (last == 0L) {
            canCheckInToday.value = true
            currentDayToClaim.value = 1
            return
        }

        if (isSameDay(last, now)) {
            canCheckInToday.value = false
            currentDayToClaim.value = if (streak > 0) streak else 1
        } else if (isYesterday(last, now)) {
            canCheckInToday.value = true
            currentDayToClaim.value = if (streak >= 7) 1 else streak + 1
        } else {
            // Streak broken
            canCheckInToday.value = true
            currentDayToClaim.value = 1
            if (streak != 0) {
                _currentStreak.value = 0
                prefs?.edit()?.putInt("current_streak", 0)?.apply()
            }
        }
    }

    fun claimCheckIn(): BigDecimal? {
        refreshState()
        if (!canCheckInToday.value) return null

        val day = currentDayToClaim.value
        val reward = rewards[day - 1]

        val now = System.currentTimeMillis()
        
        _lastCheckInTime.value = now
        _currentStreak.value = day
        
        prefs?.edit()?.apply {
            putLong("last_checkin_time", now)
            putInt("current_streak", day)
        }?.apply()
        
        ProfileManager.addBalance(reward)
        HistoryManager.addHistory(HistoryType.CLAIM, "🗓️ DAILY CHECK-IN", "Hadiah Hari ke-$day", "+${reward.toNXFormat()}")
        
        refreshState()
        return reward
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
        cal2.add(Calendar.DAY_OF_YEAR, -1)
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
