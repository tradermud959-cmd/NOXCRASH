package com.example

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

enum class HistoryType {
    MINING, CLAIM, PURCHASE, WARNING }


data class HistoryItem(
    val id: String,
    val type: HistoryType,
    val title: String,
    val description: String,
    val valueLabel: String,
    val timestamp: Long
)

object HistoryManager {
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "noxcrash_history"
    private val KEY_HISTORY = "history_list"
    
    private val _historyList = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyList: StateFlow<List<HistoryItem>> = _historyList.asStateFlow()
    
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, HistoryItem::class.java)
    private val adapter = moshi.adapter<List<HistoryItem>>(listType)

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadHistory()
    }
    
    private fun loadHistory() {
        val json = prefs.getString(KEY_HISTORY, null)
        if (json != null) {
            try {
                val list = adapter.fromJson(json) ?: emptyList()
                _historyList.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun saveHistory() {
        val json = adapter.toJson(_historyList.value)
        prefs.edit().putString(KEY_HISTORY, json).apply()
    }
    
    fun addHistory(type: HistoryType, title: String, description: String, valueLabel: String) {
        val item = HistoryItem(
            id = UUID.randomUUID().toString(),
            type = type,
            title = title,
            description = description,
            valueLabel = valueLabel,
            timestamp = System.currentTimeMillis()
        )
        val currentList = _historyList.value.toMutableList()
        currentList.add(0, item) // Add to top
        if (currentList.size > 100) { // Keep last 100 items
            currentList.removeAt(currentList.size - 1)
        }
        _historyList.value = currentList
        saveHistory()
    }


    fun reload() { loadHistory() }
}
