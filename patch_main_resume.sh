sed -i '/override fun onDestroy()/i \
  override fun onResume() {\
    super.onResume()\
    NoxNotificationManager.init(this)\
  }\
' app/src/main/java/com/example/MainActivity.kt
