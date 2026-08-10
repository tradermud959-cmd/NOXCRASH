package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

data class AIConfig(val type: AIType, val name: String, val durationDays: Long, val price: java.math.BigDecimal, val priceString: String)

object AIEconomy {
    val scout = AIConfig(AIType.SCOUT, "AI Scout", 3, java.math.BigDecimal("12000"), "12.000")
    val smart = AIConfig(AIType.SMART, "AI Smart", 7, java.math.BigDecimal("20000"), "20.000")
    val pro = AIConfig(AIType.PRO, "AI Pro", 14, java.math.BigDecimal("32000"), "32.000")
    val void = AIConfig(AIType.VOID, "AI Void", 30, java.math.BigDecimal("48000"), "48.000")
    
    fun getConfig(type: AIType): AIConfig = when(type) {
        AIType.SCOUT -> scout
        AIType.SMART -> smart
        AIType.PRO -> pro
        AIType.VOID -> void
    }
}
enum class AIType {
    SCOUT, SMART, PRO, VOID
}

enum class AIState {
    NO_AI, AI_SCOUT_ACTIVE, AI_SMART_ACTIVE, AI_PRO_ACTIVE, AI_VOID_ACTIVE
}

data class ActiveAI(
    val type: AIType,
    val name: String,
    val durationDays: Long,
    val price: BigDecimal,
    val startedAt: Long,
    val expiresAt: Long
)

data class AIHistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: AIType,
    val name: String,
    val status: String, // "AKTIF", "SELESAI"
    val startedAt: Long,
    val expiresAt: Long,
    val durationDays: Long,
    val price: BigDecimal
)

object AIManager {
    private var prefs: SharedPreferences? = null
    
    private val _aiState = MutableStateFlow(AIState.NO_AI)
    val aiState: StateFlow<AIState> = _aiState.asStateFlow()
    
    private val _activeAI = MutableStateFlow<ActiveAI?>(null)
    val activeAI: StateFlow<ActiveAI?> = _activeAI.asStateFlow()

    private val _aiHistory = MutableStateFlow<List<AIHistoryItem>>(emptyList())
    val aiHistory: StateFlow<List<AIHistoryItem>> = _aiHistory.asStateFlow()

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("ai_prefs", Context.MODE_PRIVATE)
            loadState()
            loadHistory()
        }
    }

    private fun loadState() {
        val prefs = this.prefs ?: return
        val stateStr = prefs.getString("ai_state", AIState.NO_AI.name) ?: AIState.NO_AI.name
        val state = AIState.valueOf(stateStr)
        
        if (state != AIState.NO_AI) {
            val typeStr = prefs.getString("ai_type", "") ?: ""
            val name = prefs.getString("ai_name", "") ?: ""
            val durationDays = prefs.getLong("ai_duration", 0)
            val priceStr = prefs.getString("ai_price", "0") ?: "0"
            val startedAt = prefs.getLong("ai_started_at", 0)
            val expiresAt = prefs.getLong("ai_expires_at", 0)
            
            if (typeStr.isNotEmpty()) {
                val type = AIType.valueOf(typeStr)
                val ai = ActiveAI(type, name, durationDays, BigDecimal(priceStr), startedAt, expiresAt)
                
                val currentTime = System.currentTimeMillis()
                if (currentTime >= expiresAt) {
                    // Expired
                    setAIState(AIState.NO_AI)
                    _activeAI.value = null
                    updateHistoryStatus(startedAt, "SELESAI")
                NoxNotificationManager.addNotification(
                    NoxNotificationType.AI_MODE,
                    "AI MODE SELESAI",
                    "AI Mode telah menyelesaikan proses otomatisasinya. Cek hasil aktivitasmu."
                )
                } else {
                    _activeAI.value = ai
                    _aiState.value = state
                }
            } else {
                setAIState(AIState.NO_AI)
            }
        } else {
            _aiState.value = AIState.NO_AI
        }
    }

    private fun loadHistory() {
        val prefs = this.prefs ?: return
        val historyJson = prefs.getString("ai_history", "[]") ?: "[]"
        try {
            val items = org.json.JSONArray(historyJson)
            val list = mutableListOf<AIHistoryItem>()
            for (i in 0 until items.length()) {
                val obj = items.getJSONObject(i)
                list.add(
                    AIHistoryItem(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        type = AIType.valueOf(obj.getString("type")),
                        name = obj.getString("name"),
                        status = obj.getString("status"),
                        startedAt = obj.getLong("startedAt"),
                        expiresAt = obj.getLong("expiresAt"),
                        durationDays = obj.getLong("durationDays"),
                        price = BigDecimal(obj.getString("price"))
                    )
                )
            }
            _aiHistory.value = list
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveHistory() {
        val prefs = this.prefs ?: return
        val array = org.json.JSONArray()
        for (item in _aiHistory.value) {
            val obj = org.json.JSONObject()
            obj.put("id", item.id)
            obj.put("type", item.type.name)
            obj.put("name", item.name)
            obj.put("status", item.status)
            obj.put("startedAt", item.startedAt)
            obj.put("expiresAt", item.expiresAt)
            obj.put("durationDays", item.durationDays)
            obj.put("price", item.price.toPlainString())
            array.put(obj)
        }
        prefs.edit().putString("ai_history", array.toString()).apply()
    }

    private fun updateHistoryStatus(startedAt: Long, newStatus: String) {
        val list = _aiHistory.value.toMutableList()
        val index = list.indexOfFirst { it.startedAt == startedAt }
        if (index != -1) {
            list[index] = list[index].copy(status = newStatus)
            _aiHistory.value = list
            saveHistory()
        }
    }

    private fun setAIState(state: AIState) {
        _aiState.value = state
        prefs?.edit()?.putString("ai_state", state.name)?.apply()
    }

    fun purchaseAI(type: AIType, name: String, durationDays: Long, price: BigDecimal): Boolean {
        if (_aiState.value != AIState.NO_AI) {
            return false // Another AI is active
        }
        
        // Deduct balance
        if (!ProfileManager.updateBalance(price)) {
            return false // Insufficient balance
        }

        val currentTime = System.currentTimeMillis()
        val expiresAt = currentTime + (durationDays * 24 * 60 * 60 * 1000)
        
        val ai = ActiveAI(type, name, durationDays, price, currentTime, expiresAt)
        _activeAI.value = ai
        
        prefs?.edit()?.apply {
            putString("ai_type", type.name)
            putString("ai_name", name)
            putLong("ai_duration", durationDays)
            putString("ai_price", price.toPlainString())
            putLong("ai_started_at", currentTime)
            putLong("ai_expires_at", expiresAt)
        }?.apply()

        val newState = when(type) {
            AIType.SCOUT -> AIState.AI_SCOUT_ACTIVE
            AIType.SMART -> AIState.AI_SMART_ACTIVE
            AIType.PRO -> AIState.AI_PRO_ACTIVE
            AIType.VOID -> AIState.AI_VOID_ACTIVE
        }
        setAIState(newState)

        // Add to history
        val historyItem = AIHistoryItem(
            type = type,
            name = name,
            status = "AKTIF",
            startedAt = currentTime,
            expiresAt = expiresAt,
            durationDays = durationDays,
            price = price
        )
        _aiHistory.value = listOf(historyItem) + _aiHistory.value
        saveHistory()

        return true
    }

    fun reload() {
        loadState()
        loadHistory()
    }
    fun refreshState() {
        if (_aiState.value != AIState.NO_AI) {
            val expiresAt = _activeAI.value?.expiresAt ?: 0
            if (System.currentTimeMillis() >= expiresAt) {
                val startedAt = _activeAI.value?.startedAt ?: 0
                setAIState(AIState.NO_AI)
                _activeAI.value = null
                prefs?.edit()?.apply {
                    remove("ai_type")
                    remove("ai_name")
                    remove("ai_duration")
                    remove("ai_price")
                    remove("ai_started_at")
                    remove("ai_expires_at")
                }?.apply()
                updateHistoryStatus(startedAt, "SELESAI")
                NoxNotificationManager.addNotification(
                    NoxNotificationType.AI_MODE,
                    "AI MODE SELESAI",
                    "AI Mode telah menyelesaikan proses otomatisasinya. Cek hasil aktivitasmu."
                )
            } else {
                // If AI Void is active, manage mining
                if (_aiState.value == AIState.AI_VOID_ACTIVE) {
                    processVoidAutomation()
                }
            }
        }
    }
    
    private fun processVoidAutomation() {
        if (MiningManager.miningStatus.value == MiningStatus.COMPLETED) {
            val claimSuccess = MiningManager.claimReward()
            if (claimSuccess) {
                autoPurchaseMiner()
            }
        } else if (MiningManager.miningStatus.value == MiningStatus.OFF) {
            autoPurchaseMiner()
        }
    }
    
    private fun autoPurchaseMiner() {
        val balance = ProfileManager.profileData.value.balance
        val currentBalance = BigDecimal(balance)
        
        // Define miners in order of preference (most expensive first)
        val miners = listOf(
            Triple("void_miner", "Void Miner", BigDecimal("4000")) to BigDecimal("2000"),
            Triple("ultra_miner", "Ultra Miner", BigDecimal("1500")) to BigDecimal("550"),
            Triple("fast_miner", "Fast Miner", BigDecimal("400")) to BigDecimal("120"),
            Triple("slow_miner", "Slow Miner", BigDecimal("100")) to BigDecimal("25"),
            Triple("basic_miner", "Basic Miner", BigDecimal("25")) to BigDecimal("5")
        )
        
        for (minerInfo in miners) {
            val (info, reward) = minerInfo
            val (id, name, price) = info
            
            if (currentBalance >= price) {
                if (ProfileManager.updateBalance(price)) {
                    StatisticsManager.addPurchase()
                    HistoryManager.addHistory(HistoryType.PURCHASE, "🛒 $name", "Pembelian otomatis (AI VOID)", "-${price.toNXFormat()}")
                    MiningManager.startMining(id, name, reward, 24)
                    break
                }
            }
        }
    }
}
