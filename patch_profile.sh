sed -i 's/String.format(java.util.Locale.US, "%.15f NX", newBalance)/newBalance.toNXFormat()/g' app/src/main/java/com/example/ProfileManager.kt
