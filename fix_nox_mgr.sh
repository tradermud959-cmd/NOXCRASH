sed -i '/    val hasUnread: Boolean/,/    }/ {
  /}/d
}' app/src/main/java/com/example/NoxNotificationManager.kt
echo "}" >> app/src/main/java/com/example/NoxNotificationManager.kt
