package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.FileItem
import com.example.model.FileType
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerScreen(viewModel: FlashShareViewModel) {
  val files by viewModel.files.collectAsState()
  var selectedTab by remember { mutableStateOf<FileType?>(null) }
  var searchQuery by remember { mutableStateOf("") }
  var isSelectAll by remember { mutableStateOf(false) }

  val filteredFiles = files.filter { file ->
    val matchesTab = selectedTab == null || file.type == selectedTab
    val matchesSearch = file.name.contains(searchQuery, ignoreCase = true)
    matchesTab && matchesSearch
  }

  val selectedCount = files.count { it.isSelected }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("File Manager & Transfer", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(AppScreen.Home) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
          }
        },
        actions = {
          TextButton(onClick = {
            isSelectAll = !isSelectAll
            viewModel.selectAllFiles(isSelectAll)
          }) {
            Text(if (isSelectAll) "Deselect All" else "Select All", color = MaterialTheme.colorScheme.primary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    bottomBar = {
      if (selectedCount > 0) {
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          tonalElevation = 8.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("$selectedCount files selected", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
              Text("Ready for 100 MB/s P2P transfer", fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.primary)
            }
            Button(
              onClick = { viewModel.navigateTo(AppScreen.SendReceive) },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier.testTag("send_selected_button")
            ) {
              Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Send Now", color = MaterialTheme.colorScheme.onPrimary)
            }
          }
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)
    ) {
      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search files, apps, media...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("file_search_input"),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = MaterialTheme.colorScheme.primary,
          unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Category Tabs
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedTab == null,
            onClick = { selectedTab = null },
            label = { Text("All (${files.size})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("chip_all")
          )
        }
        item {
          FilterChip(
            selected = selectedTab == FileType.APP,
            onClick = { selectedTab = FileType.APP },
            label = { Text("Apps (${files.count { it.type == FileType.APP }})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("chip_apps")
          )
        }
        item {
          FilterChip(
            selected = selectedTab == FileType.VIDEO,
            onClick = { selectedTab = FileType.VIDEO },
            label = { Text("Videos (${files.count { it.type == FileType.VIDEO }})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("chip_videos")
          )
        }
        item {
          FilterChip(
            selected = selectedTab == FileType.MUSIC,
            onClick = { selectedTab = FileType.MUSIC },
            label = { Text("Music (${files.count { it.type == FileType.MUSIC }})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("chip_music")
          )
        }
        item {
          FilterChip(
            selected = selectedTab == FileType.PHOTO,
            onClick = { selectedTab = FileType.PHOTO },
            label = { Text("Photos (${files.count { it.type == FileType.PHOTO }})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("chip_photos")
          )
        }
        item {
          FilterChip(
            selected = selectedTab == FileType.DOCUMENT,
            onClick = { selectedTab = FileType.DOCUMENT },
            label = { Text("Docs (${files.count { it.type == FileType.DOCUMENT }})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.testTag("chip_docs")
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (filteredFiles.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("No files found", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      } else {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(filteredFiles, key = { it.id }) { file ->
            FileItemRow(file = file, onToggle = { viewModel.toggleFileSelection(file.id) })
          }
        }
      }
    }
  }
}

@Composable
fun FileItemRow(file: FileItem, onToggle: () -> Unit) {
  val icon = when (file.type) {
    FileType.APP -> Icons.Default.Android
    FileType.VIDEO -> Icons.Default.VideoFile
    FileType.MUSIC -> Icons.Default.AudioFile
    FileType.PHOTO -> Icons.Default.Image
    FileType.DOCUMENT -> Icons.Default.Description
  }

  val iconColor = when (file.type) {
    FileType.APP -> Color(0xFF00E676)
    FileType.VIDEO -> Color(0xFF00E5FF)
    FileType.MUSIC -> Color(0xFFFF6D00)
    FileType.PHOTO -> Color(0xFFE040FB)
    FileType.DOCUMENT -> Color(0xFFFFEA00)
  }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (file.isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onToggle() }
      .testTag("file_item_${file.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(iconColor.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(file.name, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
        Spacer(modifier = Modifier.height(2.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(file.size, fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("•", fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(file.dateAdded, fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }

      Checkbox(
        checked = file.isSelected,
        onCheckedChange = { onToggle() },
        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
      )
    }
  }
}
