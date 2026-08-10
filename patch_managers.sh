sed -i '/fun updateBalance/i \
    fun reload() {\
        _profileData.value = loadProfile()\
    }' app/src/main/java/com/example/ProfileManager.kt

sed -i '/fun refreshState()/i \
    fun reload() {\
        loadState()\
    }' app/src/main/java/com/example/MiningManager.kt

sed -i '/fun refreshState()/i \
    fun reload() {\
        loadState()\
        loadHistory()\
    }' app/src/main/java/com/example/AIManager.kt

sed -i '/fun clearHistory()/i \
    fun reload() {\
        loadHistory()\
    }' app/src/main/java/com/example/HistoryManager.kt

sed -i '/fun getStats()/i \
    fun reload() {\
        loadStats()\
    }' app/src/main/java/com/example/StatisticsManager.kt
