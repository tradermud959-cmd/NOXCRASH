package com.example

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

private val Context.dataStore by preferencesDataStore(name = "supply_prefs")

object SupplyManager {
    private const val INITIAL_SUPPLY = "50000000"
    private val KEY_SUPPLY = stringPreferencesKey("nx_supply")

    private val _currentSupply = MutableStateFlow(BigDecimal(INITIAL_SUPPLY))
    val currentSupply: StateFlow<BigDecimal> = _currentSupply.asStateFlow()

    private var appContext: Context? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    fun init(context: Context) {
        if (appContext == null) {
            appContext = context.applicationContext
            scope.launch {
                loadSupply()
            }
        }
    }

    private suspend fun loadSupply() {
        val context = appContext ?: return
        val preferences = context.dataStore.data.first()
        val supplyStr = preferences[KEY_SUPPLY] ?: INITIAL_SUPPLY
        _currentSupply.value = try {
            BigDecimal(supplyStr)
        } catch (e: Exception) {
            BigDecimal(INITIAL_SUPPLY)
        }
    }

    fun deductSupply(amount: BigDecimal): Boolean {
        val current = _currentSupply.value
        if (current >= amount) {
            val newSupply = current.subtract(amount)
            _currentSupply.value = newSupply
            saveSupplyAsync(newSupply)
            return true
        }
        return false
    }

    private fun saveSupplyAsync(supply: BigDecimal) {
        scope.launch {
            appContext?.dataStore?.edit { preferences ->
                preferences[KEY_SUPPLY] = supply.toPlainString()
            }
        }
    }

    fun getSupply(): BigDecimal = _currentSupply.value
}
