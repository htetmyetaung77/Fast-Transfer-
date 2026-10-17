package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

data class ActionCardItem(
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val color: Color,
  val screen: AppScreen,
  val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: FlashShareViewModel) {
  val actions = listOf(
    ActionCardItem("Send", "Share files instantly", Icons.Default.Send, Color(0xFF00E5FF), AppScreen.SendReceive, "home_send_button"),
    ActionCardItem("Receive", "Connect & accept files", Icons.Default.Download, Color(0xFF00E676), AppScreen.SendReceive, "home_receive_button"),
    ActionCardItem("Files", "Browse local storage", Icons.Default.Folder, Color(0xFFFF6D00), AppScreen.FileManager, "home_files_button"),
    ActionCardItem("Apps", "APK backup & sharing", Icons.Default.Android, Color(0xFFE040FB), AppScreen.AppsShare, "home_apps_button"),
    ActionCardItem("Media Hub", "Videos & music player", Icons.Default.PlayCircle, Color(0xFFFFEA00), AppScreen.MediaHub, "home_media_button"),
    ActionCardItem("Group Chat", "Broadcast & chat", Icons.Default.Group, Color(0xFFFF5252), AppScreen.GroupChat, "home_chat_button")
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("FlashShare 100 MB/s", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
        actions = {
          IconButton(onClick = { viewModel.navigateTo(AppScreen.SendReceive) }) {
            Icon(Icons.Default.Radar, contentDescription = "Radar", tint = MaterialTheme.colorScheme.primary)
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
      // Hero Banner Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.horizontalGradient(
                listOf(Color(0xFF00B8D4).copy(alpha = 0.2f), Color(0xFFFF6D00).copy(alpha = 0.2f))
              )
            )
            .padding(20.dp),
          contentAlignment = Alignment.CenterStart
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(12.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF00E676))
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Wi-Fi Direct P2P Active", fontSize = 12.dp.value.sp, color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Lightning Transfer Speed", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Up to 100 MB/s offline sharing without mobile data", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      Text("Explore Features", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
      Spacer(modifier = Modifier.height(12.dp))

      // Grid Actions
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(actions) { action ->
          FeatureCard(action = action, onClick = { viewModel.navigateTo(action.screen) })
        }
      }
    }
  }
}

@Composable
fun FeatureCard(action: ActionCardItem, onClick: () -> Unit) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier
      .fillMaxWidth()
      .height(130.dp)
      .clickable { onClick() }
      .testTag(action.testTag)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(action.color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(action.icon, contentDescription = null, tint = action.color, modifier = Modifier.size(22.dp))
      }
      Column {
        Text(action.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(2.dp))
        Text(action.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
      }
    }
  }
}
