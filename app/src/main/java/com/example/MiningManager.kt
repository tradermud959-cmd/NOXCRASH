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
    val targetReward: BigDecimal,
    val hashrate: BigDecimal,
    val startedAt: Long
) {
    fun calculateCurrentReward(currentTime: Long): BigDecimal {
        val elapsedMs = (currentTime - startedAt).coerceAtLeast(0)
        val elapsedSec = BigDecimal(elapsedMs).divide(BigDecimal("1000"), 15, java.math.RoundingMode.DOWN)
        val ratePerSec = NoxEconomyConfig.getRewardRatePerSecond(hashrate)
        val generated = ratePerSec.multiply(elapsedSec)
        return generated.min(targetReward)
    }
}

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
            val targetRewardStr = prefs.getString("miner_target_reward", "0") ?: "0"
            val hashrateStr = prefs.getString("miner_hashrate", "0") ?: "0"
            val startedAt = prefs.getLong("miner_started_at", 0)

            if (id.isNotEmpty()) {
                val miner = ActiveMiner(
                    id = id,
                    name = name,
                    targetReward = BigDecimal(targetRewardStr),
                    hashrate = BigDecimal(hashrateStr),
                    startedAt = startedAt
                )
                
                val currentTime = System.currentTimeMillis()
                val currentReward = miner.calculateCurrentReward(currentTime)
                val isCompleted = currentReward >= miner.targetReward || SupplyManager.getSupply() <= BigDecimal.ZERO
                
                if (isCompleted && status == MiningStatus.ACTIVE) {
                    _activeMiner.value = miner
                    setMiningStatus(MiningStatus.COMPLETED)
                    NoxNotificationManager.addNotification(
                        NoxNotificationType.MINER,
                        "MINER SELESAI",
                        "Proses mining oleh ${miner.name} telah selesai. Cek detail aktivitasmu."
                    )
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

    // Keep durationHours in signature to not break existing calls, but ignore it.
    fun startMining(id: String, name: String, targetReward: BigDecimal, durationHours: Long = 0): Boolean {
        if (_miningStatus.value != MiningStatus.OFF) {
            return false // Already mining
        }

        val currentTime = System.currentTimeMillis()
        val hashrate = NoxEconomyConfig.getHashrateForMiner(id)
        
        val miner = ActiveMiner(
            id = id,
            name = name,
            targetReward = targetReward,
            hashrate = hashrate,
            startedAt = currentTime
        )

        _activeMiner.value = miner
        
        prefs?.edit()?.apply {
            putString("miner_id", id)
            putString("miner_name", name)
            putString("miner_target_reward", targetReward.toPlainString())
            putString("miner_hashrate", hashrate.toPlainString())
            putLong("miner_started_at", currentTime)
        }?.apply()

        if (id == "free_miner") {
            _lastFreeMinerUsedAt.value = currentTime
            prefs?.edit()?.putLong("last_free_miner_used_at", currentTime)?.apply()
        }

        setMiningStatus(MiningStatus.ACTIVE)
        
        HistoryManager.addHistory(HistoryType.MINING, "⛏️ ${name.uppercase()}", "Mining dimulai", "Target: ${targetReward.toShortNXFormat()}")
        
        // Add a generic session log since we don't have exact duration anymore
        StatisticsManager.addSession(24) // Dummy duration for backward compatibility
        
        return true
    }

    fun claimReward(): Boolean {
        if (_miningStatus.value != MiningStatus.COMPLETED) {
            return false
        }

        val miner = _activeMiner.value ?: return false
        val currentTime = System.currentTimeMillis()
        val calculatedReward = miner.calculateCurrentReward(currentTime)
        
        var rewardToClaim = calculatedReward

        val currentSupply = SupplyManager.getSupply()
        if (currentSupply <= BigDecimal.ZERO) {
            rewardToClaim = BigDecimal.ZERO
        } else if (rewardToClaim > currentSupply) {
            rewardToClaim = currentSupply
        }

        val success = ProfileManager.addBalance(rewardToClaim)
        if (success) {
            if (rewardToClaim > BigDecimal.ZERO) {
                SupplyManager.deductSupply(rewardToClaim)
            }
            HistoryManager.addHistory(HistoryType.CLAIM, "💰 REWARD CLAIM", "Reward mining diterima", "+${rewardToClaim.toShortNXFormat()}")
            StatisticsManager.addReward(rewardToClaim)
            
            _activeMiner.value = null
            prefs?.edit()?.apply {
                remove("miner_id")
                remove("miner_name")
                remove("miner_target_reward")
                remove("miner_hashrate")
                remove("miner_started_at")
            }?.apply()
            setMiningStatus(MiningStatus.OFF)
            return true
        }
        return false
    }
    
    fun reload() {
        loadState()
    }

    fun refreshState() {
        if (_miningStatus.value == MiningStatus.ACTIVE) {
            val miner = _activeMiner.value ?: return
            val currentReward = miner.calculateCurrentReward(System.currentTimeMillis())
            val supply = SupplyManager.getSupply()
            if (currentReward >= miner.targetReward || supply <= BigDecimal.ZERO) {
                setMiningStatus(MiningStatus.COMPLETED)
                NoxNotificationManager.addNotification(
                    NoxNotificationType.MINER,
                    "MINER SELESAI",
                    "Proses mining oleh ${miner.name} telah selesai. Cek detail aktivitasmu."
                )
            }
        }
    }
}
