sed -i 's/private fun loadAndCleanNotifications()/fun cleanOldNotifications() {\n        loadAndCleanNotifications()\n    }\n\n    private fun loadAndCleanNotifications()/g' app/src/main/java/com/example/NoxNotificationManager.kt
sed -i 's/NoxNotificationManager.init(this)/NoxNotificationManager.cleanOldNotifications()/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/super.onCreate(savedInstanceState)/super.onCreate(savedInstanceState)\n    NoxNotificationManager.init(this)/g' app/src/main/java/com/example/MainActivity.kt
