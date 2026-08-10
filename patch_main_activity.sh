sed -i 's/import android.os.Bundle/import android.os.Bundle\nimport android.os.Build\nimport androidx.core.app.ActivityCompat\nimport android.content.pm.PackageManager\nimport android.Manifest/g' app/src/main/java/com/example/MainActivity.kt
sed -i '/enableEdgeToEdge()/a \
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {\
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {\
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)\
            }\
        }' app/src/main/java/com/example/MainActivity.kt
