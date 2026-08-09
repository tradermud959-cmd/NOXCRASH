package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

enum class MiningStatus {
    OFF, ACTIVE, COMPLETED
}

data class ActiveMiner(
    val id: String,
    val name: String,
    val reward: BigDecimal,
    val durationHours: Long,
    val startedAt: Long,
    val endsAt: Long
)

object MiningManager {
    private var prefs: SharedPreferences? = null

    private val _miningStatus = MutableStateFlow(MiningStatus.OFF)
    val miningStatus: StateFlow<MiningStatus> = _miningStatus.asStateFlow()

    private val _activeMiner = MutableStateFlow<ActiveMiner?>(null)
    val activeMiner: StateFlow<ActiveMiner?> = _activeMiner.asStateFlow()

    private val _lastFreeMinerUsedAt = MutableStateFlow<Long>(0)
    val lastFreeMinerUsedAt: StateFlow<Long> = _lastFreeMinerUsedAt.asStateFlow()

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("mining_prefs", Context.MODE_PRIVATE)
            loadState()
        }
    }

    private fun loadState() {
        val prefs = this.prefs ?: return
        val statusStr = prefs.getString("mining_status", MiningStatus.OFF.name) ?: MiningStatus.OFF.name
        val status = MiningStatus.valueOf(statusStr)
        
        _lastFreeMinerUsedAt.value = prefs.getLong("last_free_miner_used_at", 0)

        if (status != MiningStatus.OFF) {
            val id = prefs.getString("miner_id", "") ?: ""
            val name = prefs.getString("miner_name", "") ?: ""
            val rewardStr = prefs.getString("miner_reward", "0") ?: "0"
            val duration = prefs.getLong("miner_duration", 0)
            val startedAt = prefs.getLong("miner_started_at", 0)
            val endsAt = prefs.getLong("miner_ends_at", 0)

            if (id.isNotEmpty()) {
                val miner = ActiveMiner(
                    id = id,
                    name = name,
                    reward = BigDecimal(rewardStr),
                    durationHours = duration,
                    startedAt = startedAt,
                    endsAt = endsAt
                )
                
                val currentTime = System.currentTimeMillis()
                if (currentTime >= endsAt && status == MiningStatus.ACTIVE) {
                    _activeMiner.value = miner
                    setMiningStatus(MiningStatus.COMPLETED)
                } else {
                    _activeMiner.value = miner
                    _miningStatus.value = status
                }
            } else {
                setMiningStatus(MiningStatus.OFF)
            }
        } else {
             _miningStatus.value = MiningStatus.OFF
        }
    }

    private fun setMiningStatus(status: MiningStatus) {
        _miningStatus.value = status
        prefs?.edit()?.putString("mining_status", status.name)?.apply()
    }

    fun startMining(id: String, name: String, reward: BigDecimal, durationHours: Long): Boolean {
        if (_miningStatus.value != MiningStatus.OFF) {
            return false // Already mining
        }

        val currentTime = System.currentTimeMillis()
        val endsAt = currentTime + (durationHours * 60 * 60 * 1000)

        val miner = ActiveMiner(
            id = id,
            name = name,
            reward = reward,
            durationHours = durationHours,
            startedAt = currentTime,
            endsAt = endsAt
        )

        _activeMiner.value = miner
        
        prefs?.edit()?.apply {
            putString("miner_id", id)
            putString("miner_name", name)
            putString("miner_reward", reward.toPlainString())
            putLong("miner_duration", durationHours)
            putLong("miner_started_at", currentTime)
            putLong("miner_ends_at", endsAt)
        }?.apply()

        if (id == "free_miner") {
            _lastFreeMinerUsedAt.value = currentTime
            prefs?.edit()?.putLong("last_free_miner_used_at", currentTime)?.apply()
        }

        setMiningStatus(MiningStatus.ACTIVE)
        
        if (id == "free_miner") {
            HistoryManager.addHistory(HistoryType.MINING, "⛏️ FREE MINER", "Mining dimulai", "+${reward.toPlainString()} NX / ${durationHours} JAM")
        } else {
            HistoryManager.addHistory(HistoryType.MINING, "⛏️ ${name.uppercase()}", "Mining dimulai", "+${reward.toPlainString()} NX / ${durationHours} JAM")
        }
        StatisticsManager.addSession(durationHours)
        
        return true
    }

    fun claimReward(): Boolean {
        if (_miningStatus.value != MiningStatus.COMPLETED) {
            return false
        }

        val miner = _activeMiner.value ?: return false
        val reward = miner.reward

        // Try to add reward to profile balance
        val success = ProfileManager.addBalance(reward)
        if (success) {
            HistoryManager.addHistory(HistoryType.CLAIM, "💰 REWARD CLAIM", "Reward mining diterima", "+${reward.toPlainString()} NX")
            StatisticsManager.addReward(reward)
            
            // Reset mining state
            _activeMiner.value = null
            prefs?.edit()?.apply {
                remove("miner_id")
                remove("miner_name")
                remove("miner_reward")
                remove("miner_duration")
                remove("miner_started_at")
                remove("miner_ends_at")
            }?.apply()
            setMiningStatus(MiningStatus.OFF)
            return true
        }
        return false
    }
    
    fun refreshState() {
        if (_miningStatus.value == MiningStatus.ACTIVE) {
            val endsAt = _activeMiner.value?.endsAt ?: 0
            if (System.currentTimeMillis() >= endsAt) {
                setMiningStatus(MiningStatus.COMPLETED)
            }
        }
    }
}
