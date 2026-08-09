package com.example

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@Composable
fun MusicListScreen(navController: NavController) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(checkAudioPermission(context)) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasPermission = checkAudioPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBack(title = "MUSIK", navController = navController, color = ColorMusicAccent)
        },
        containerColor = MusicPageBg
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (hasPermission) {
                MusicListContent(navController)
            } else {
                PermissionRequestContent(
                    onRequest = {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_AUDIO
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                        permissionLauncher.launch(permission)
                    },
                    onOpenSettings = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                )
            }
        }
    }
}

fun checkAudioPermission(context: Context): Boolean {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun PermissionRequestContent(onRequest: () -> Unit, onOpenSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎵", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Izin musik belum diberikan",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Berikan izin agar musik dari perangkat dapat ditampilkan.",
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRequest,
            colors = ButtonDefaults.buttonColors(containerColor = ColorMusicAccent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("IZINKAN AKSES", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onOpenSettings) {
            Text("BUKA PENGATURAN", color = ColorMusicAccent)
        }
    }
}

@Composable
fun MusicListContent(navController: NavController) {
    val context = LocalContext.current
    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    
    val currentSong by MusicPlayerManager.currentSong.collectAsState()
    val isPlaying by MusicPlayerManager.isPlaying.collectAsState()

    LaunchedEffect(Unit) {
        songs = loadSongs(context)
        isLoading = false
    }

    val filteredSongs = remember(songs, searchQuery) {
        if (searchQuery.isEmpty()) songs else songs.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Cari musik...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = TextSecondary) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ColorMusicAccent,
                unfocusedBorderColor = BorderColor,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = ColorMusicAccent
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ColorMusicAccent)
            }
        } else if (songs.isEmpty()) {
            EmptyState("🎵", "Belum Ada Musik", "Belum ditemukan file musik di perangkat.")
        } else if (filteredSongs.isEmpty()) {
            EmptyState("🔍", "Tidak Ditemukan", "Musik tidak ditemukan untuk pencarian tersebut.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredSongs, key = { it.uri }) { song ->
                    val isSelected = currentSong?.uri == song.uri
                    SongItem(
                        song = song,
                        isSelected = isSelected,
                        isPlaying = isSelected && isPlaying,
                        onClick = {
                            if (isSelected) {
                                MusicPlayerManager.togglePlayPause(context)
                            } else {
                                MusicPlayerManager.playSong(context, song)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SongItem(song: Song, isSelected: Boolean, isPlaying: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) Color(0x1AD500F9) else Color.Transparent
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(bgColor)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (isSelected) ColorMusicAccent else MusicSurface, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isPlaying) {
                Text("▶", color = Color.White, fontSize = 16.sp)
            } else {
                Text("🎵", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = if (isSelected) ColorMusicAccent else TextPrimary,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.duration,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

suspend fun loadSongs(context: Context): List<Song> = withContext(Dispatchers.IO) {
    val songs = mutableListOf<Song>()
    val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION
    )

    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

    try {
        context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                var title = cursor.getString(titleColumn)
                val durationMs = cursor.getLong(durationColumn)
                
                if (title.isNullOrBlank()) {
                    title = name ?: "Unknown"
                }
                
                val contentUri: Uri = ContentUris.withAppendedId(uri, id)
                
                val durationStr = if (durationMs > 0) {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) - TimeUnit.MINUTES.toSeconds(minutes)
                    String.format("%02d:%02d", minutes, seconds)
                } else {
                    "--:--"
                }
                
                songs.add(Song(contentUri.toString(), title, durationStr))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return@withContext songs
}
