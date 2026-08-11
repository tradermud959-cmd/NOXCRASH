sed -i '/fun claimReward(): Boolean {/,/^    }/c\
    fun claimReward(): Boolean {\
        if (_miningStatus.value != MiningStatus.COMPLETED) {\
            return false\
        }\
\
        val miner = _activeMiner.value ?: return false\
        var rewardToClaim = miner.reward\
\
        val currentSupply = SupplyManager.getSupply()\
        if (currentSupply <= BigDecimal.ZERO) {\
            rewardToClaim = BigDecimal.ZERO\
        } else if (rewardToClaim > currentSupply) {\
            rewardToClaim = currentSupply\
        }\
\
        // Try to add reward to profile balance\
        val success = ProfileManager.addBalance(rewardToClaim)\
        if (success) {\
            if (rewardToClaim > BigDecimal.ZERO) {\
                SupplyManager.deductSupply(rewardToClaim)\
            }\
            HistoryManager.addHistory(HistoryType.CLAIM, "💰 REWARD CLAIM", "Reward mining diterima", "+${rewardToClaim.toShortNXFormat()}")\
            StatisticsManager.addReward(rewardToClaim)\
            \
            // Reset mining state\
            _activeMiner.value = null\
            prefs?.edit()?.apply {\
                remove("miner_id")\
                remove("miner_name")\
                remove("miner_reward")\
                remove("miner_duration")\
                remove("miner_started_at")\
                remove("miner_ends_at")\
            }?.apply()\
            setMiningStatus(MiningStatus.OFF)\
            return true\
        }\
        return false\
    }' app/src/main/java/com/example/MiningManager.kt
