package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BackupRestoreManager {
    private const val PREFS_URI = "backup_uri_prefs"
    private const val KEY_URI = "saved_tree_uri"
    
    val PREF_NAMES = listOf(
        "profile_prefs",
        "mining_prefs",
        "noxcrash_stats",
        "noxcrash_history",
        "music_prefs",
        "ai_prefs"
    )

    fun getSavedTreeUri(context: Context): Uri? {
        val prefs = context.getSharedPreferences(PREFS_URI, Context.MODE_PRIVATE)
        val uriStr = prefs.getString(KEY_URI, null) ?: return null
        return Uri.parse(uriStr)
    }

    fun saveTreeUri(context: Context, uri: Uri) {
        val prefs = context.getSharedPreferences(PREFS_URI, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_URI, uri.toString()).apply()
        
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        try {
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun performBackup(context: Context, treeUri: Uri): String? {
        try {
            val rootDoc = DocumentFile.fromTreeUri(context, treeUri) ?: return null
            
            var noxcrashDir = rootDoc.findFile("Noxcrash")
            if (noxcrashDir == null) {
                noxcrashDir = rootDoc.createDirectory("Noxcrash")
            }
            if (noxcrashDir == null) return null
            
            var backupDir = noxcrashDir.findFile("backup")
            if (backupDir == null) {
                backupDir = noxcrashDir.createDirectory("backup")
            }
            if (backupDir == null) return null
            
            val dateFormat = SimpleDateFormat("dd-MM-yyyy_HH-mm", Locale.US)
            val fileName = "${dateFormat.format(Date())}.json"
            
            val fileDoc = backupDir.createFile("application/json", fileName) ?: return null
            
            val fullData = JSONObject()
            
            for (prefName in PREF_NAMES) {
                val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                val allEntries = prefs.all
                val prefJson = JSONObject()
                for ((key, value) in allEntries) {
                    prefJson.put(key, value)
                }
                fullData.put(prefName, prefJson)
            }
            
            context.contentResolver.openOutputStream(fileDoc.uri)?.use { outputStream ->
                outputStream.write(fullData.toString(4).toByteArray())
            }
            
            return fileName
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun performRestore(context: Context, fileUri: Uri): Boolean {
        try {
            val jsonString = context.contentResolver.openInputStream(fileUri)?.bufferedReader()?.use { it.readText() } ?: return false
            val fullData = JSONObject(jsonString)
            
            // Basic validation
            var hasValidKeys = false
            for (prefName in PREF_NAMES) {
                if (fullData.has(prefName)) {
                    hasValidKeys = true
                    break
                }
            }
            if (!hasValidKeys) return false
            
            for (prefName in PREF_NAMES) {
                if (fullData.has(prefName)) {
                    val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.clear()
                    
                    val prefJson = fullData.getJSONObject(prefName)
                    val keys = prefJson.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        when (val value = prefJson.get(key)) {
                            is String -> editor.putString(key, value)
                            is Int -> editor.putInt(key, value)
                            is Long -> editor.putLong(key, value)
                            is Float -> editor.putFloat(key, value)
                            is Boolean -> editor.putBoolean(key, value)
                        }
                    }
                    editor.apply()
                }
            }
            
            reloadAllManagers()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun performReset(context: Context) {
        for (prefName in PREF_NAMES) {
            val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }
        reloadAllManagers()
    }
    
    private fun reloadAllManagers() {
        ProfileManager.reload()
        MiningManager.reload()
        AIManager.reload()
        HistoryManager.reload()
        StatisticsManager.reload()
        // MusicPlayerManager doesn't seem to have much state to reload other than songs, but we can call it.
    }
}
