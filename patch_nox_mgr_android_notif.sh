cat << 'INNER_EOF' >> app/src/main/java/com/example/NoxNotificationManager.kt

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

            if (androidx.core.app.ActivityCompat.checkSelfPermission(appContext, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(notif.id.hashCode(), builder.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
INNER_EOF
sed -i 's/_newNotificationFlow.tryEmit(item)/_newNotificationFlow.tryEmit(item)\n        sendAndroidNotification(item)/g' app/src/main/java/com/example/NoxNotificationManager.kt
