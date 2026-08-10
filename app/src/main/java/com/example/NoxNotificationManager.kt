package com.example

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.UUID

enum class NoxNotificationType {
    MINER,
    AI_MODE,
    SYSTEM,
    WARNING
}

data class NoxNotification(
    val id: String,
    val type: NoxNotificationType,
    val title: String,
    val description: String,
    val timestamp: Long
)

object NoxNotificationManager {
    private lateinit var prefs: SharedPreferences
    private lateinit var appContext: Context
    private const val PREFS_NAME = "noxcrash_notifications"
    private const val KEY_NOTIFICATIONS = "notifications_list"

    private val _notificationsList = MutableStateFlow<List<NoxNotification>>(emptyList())
    val notificationsList: StateFlow<List<NoxNotification>> = _notificationsList.asStateFlow()

    private val _newNotificationFlow = MutableSharedFlow<NoxNotification>(extraBufferCapacity = 1)
    val newNotificationFlow: SharedFlow<NoxNotification> = _newNotificationFlow.asSharedFlow()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, NoxNotification::class.java)
    private val adapter = moshi.adapter<List<NoxNotification>>(listType)

    fun init(context: Context) {
        appContext = context.applicationContext
        prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadAndCleanNotifications()
    }

    fun cleanOldNotifications() {
        loadAndCleanNotifications()
    }

    private fun loadAndCleanNotifications() {
        val json = prefs.getString(KEY_NOTIFICATIONS, null)
        val currentTime = System.currentTimeMillis()
        val oneDayMillis = 24L * 60 * 60 * 1000

        var list: List<NoxNotification> = emptyList()
        if (json != null) {
            try {
                list = adapter.fromJson(json) ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Filter out notifications older than 24 hours
        val validList = list.filter { currentTime - it.timestamp < oneDayMillis }
        
        _notificationsList.value = validList
        
        if (validList.size != list.size) {
            saveNotifications()
        }
    }

    private fun saveNotifications() {
        val json = adapter.toJson(_notificationsList.value)
        prefs.edit().putString(KEY_NOTIFICATIONS, json).apply()
    }

    fun addNotification(type: NoxNotificationType, title: String, description: String) {
        loadAndCleanNotifications() // Ensure we clean before adding
        val item = NoxNotification(
            id = UUID.randomUUID().toString(),
            type = type,
            title = title,
            description = description,
            timestamp = System.currentTimeMillis()
        )
        
        val currentList = _notificationsList.value.toMutableList()
        currentList.add(0, item) // Add to top
        
        // Keep reasonable max list size just in case, e.g., 50
        if (currentList.size > 50) {
            currentList.removeAt(currentList.size - 1)
        }
        
        _notificationsList.value = currentList
        saveNotifications()
        
        _newNotificationFlow.tryEmit(item)
        sendAndroidNotification(item)
    }
    
    val hasUnread: Boolean
        get() = _notificationsList.value.isNotEmpty() // Simplified logic for unread indicator

    private fun sendAndroidNotification(notif: NoxNotification) {
        try {
            val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = android.app.NotificationChannel(
                    "noxcrash_channel",
                    "NoxCrash Notifications",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val builder = androidx.core.app.NotificationCompat.Builder(appContext, "noxcrash_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notif.title)
                .setContentText(notif.description)
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            if (androidx.core.content.ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(notif.id.hashCode(), builder.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
