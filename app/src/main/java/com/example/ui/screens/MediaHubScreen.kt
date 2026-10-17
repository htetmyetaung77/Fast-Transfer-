package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FileType
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaHubScreen(viewModel: FlashShareViewModel) {
  val files by viewModel.files.collectAsState()
  val mediaFiles = files.filter { it.type == FileType.VIDEO || it.type == FileType.MUSIC || it.type == FileType.PHOTO }
  var playingFile by remember { mutableStateOf(mediaFiles.firstOrNull()) }
  var isPlaying by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Media Hub & Player", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(AppScreen.Home) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)
    ) {
      // Player Preview Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFEA00).copy(alpha = 0.2f))
                .clickable { isPlaying = !isPlaying },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color(0xFFFFEA00),
                modifier = Modifier.size(36.dp)
              )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(playingFile?.name ?: "Select media to play", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(if (isPlaying) "Playing..." else "Paused", fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      Text("Local Media Library", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(mediaFiles, key = { it.id }) { file ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                playingFile = file
                isPlaying = true
              }
              .testTag("media_item_${file.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFFEA00).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  when (file.type) {
                    FileType.VIDEO -> Icons.Default.Videocam
                    FileType.MUSIC -> Icons.Default.MusicNote
                    else -> Icons.Default.Image
                  },
                  contentDescription = null,
                  tint = Color(0xFFFFEA00),
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(file.name, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Text(file.size, fontSize = 11.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              IconButton(onClick = {
                playingFile = file
                isPlaying = true
              }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = MaterialTheme.colorScheme.primary)
              }
            }
          }
        }
      }
    }
  }
}
