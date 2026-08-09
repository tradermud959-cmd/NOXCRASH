package com.example

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream

data class ProfileData(
    val username: String = "@username",
    val photoUri: String? = null,
    val coverUri: String? = null,
    val balance: String = "0.00000000 NX"
)

object ProfileManager {
    private var prefs: SharedPreferences? = null
    private lateinit var appContext: Context
    
    private val _profileData = MutableStateFlow(ProfileData())
    val profileData: StateFlow<ProfileData> = _profileData.asStateFlow()
    
    fun init(context: Context) {
        if (prefs == null) {
            appContext = context.applicationContext
            prefs = appContext.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
            _profileData.value = loadProfile()
        }
    }

    private fun loadProfile(): ProfileData {
        return ProfileData(
            username = prefs?.getString("username", "@username") ?: "@username",
            photoUri = prefs?.getString("photoUri", null),
            coverUri = prefs?.getString("coverUri", null),
            balance = prefs?.getString("balance", "0.00000000 NX") ?: "0.00000000 NX"
        )
    }
    
    fun saveProfile(data: ProfileData) {
        prefs?.edit()
            ?.putString("username", data.username)
            ?.putString("photoUri", data.photoUri)
            ?.putString("coverUri", data.coverUri)
            ?.putString("balance", data.balance)
            ?.apply()
        _profileData.value = data
    }
    
    fun addBalance(amountToAdd: java.math.BigDecimal): Boolean {
        val currentStr = _profileData.value.balance.replace(" NX", "")
        return try {
            val currentDec = java.math.BigDecimal(currentStr)
            val newBalance = currentDec.add(amountToAdd)
            val newBalanceStr = String.format(java.util.Locale.US, "%.8f NX", newBalance)
            saveProfile(_profileData.value.copy(balance = newBalanceStr))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    fun updateBalance(amountToSubtract: java.math.BigDecimal): Boolean {
        val currentStr = _profileData.value.balance.replace(" NX", "")
        return try {
            val currentDec = java.math.BigDecimal(currentStr)
            if (currentDec >= amountToSubtract) {
                val newBalance = currentDec.subtract(amountToSubtract)
                val newBalanceStr = String.format(java.util.Locale.US, "%.8f NX", newBalance)
                saveProfile(_profileData.value.copy(balance = newBalanceStr))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    fun copyImageToInternalStorage(uri: Uri, isCover: Boolean): String? {
        return try {
            val inputStream = appContext.contentResolver.openInputStream(uri) ?: return null
            val fileName = if (isCover) "cover_image_${System.currentTimeMillis()}.jpg" else "profile_image_${System.currentTimeMillis()}.jpg"
            val file = File(appContext.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            
            // Delete old file if we want to save space, but keeping it simple for now
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
