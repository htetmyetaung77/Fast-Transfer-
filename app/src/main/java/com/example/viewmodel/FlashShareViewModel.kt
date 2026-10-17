package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ChatMessage
import com.example.model.FileItem
import com.example.model.FileType
import com.example.model.NearbyDevice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AppScreen {
  object Home : AppScreen()
  object SendReceive : AppScreen()
  object FileManager : AppScreen()
  object AppsShare : AppScreen()
  object MediaHub : AppScreen()
  object GroupChat : AppScreen()
  object TransferProgress : AppScreen()
}

class FlashShareViewModel : ViewModel() {

  private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _files = MutableStateFlow<List<FileItem>>(
    listOf(
      FileItem("1", "Cyberpunk_Trailer_4K.mp4", "450 MB", FileType.VIDEO, "/storage/emulated/0/Movies/Cyberpunk_Trailer_4K.mp4", dateAdded = "2 mins ago"),
      FileItem("2", "Electronic_Beat_Mix.mp3", "12.5 MB", FileType.MUSIC, "/storage/emulated/0/Music/Electronic_Beat_Mix.mp3", dateAdded = "1 hour ago"),
      FileItem("3", "Neon_City_Wallpaper.png", "4.2 MB", FileType.PHOTO, "/storage/emulated/0/Pictures/Neon_City_Wallpaper.png", dateAdded = "Yesterday"),
      FileItem("4", "Project_Blueprint.pdf", "8.9 MB", FileType.DOCUMENT, "/storage/emulated/0/Documents/Project_Blueprint.pdf", dateAdded = "3 days ago"),
      FileItem("5", "FlashShare_v2.1.apk", "28.4 MB", FileType.APP, "/data/app/com.flashshare.apk", dateAdded = "Today"),
      FileItem("6", "Action_SciFi_Short.mkv", "820 MB", FileType.VIDEO, "/storage/emulated/0/Movies/Action_SciFi_Short.mkv", dateAdded = "Yesterday"),
      FileItem("7", "Chill_Lofi_Session.mp3", "45.1 MB", FileType.MUSIC, "/storage/emulated/0/Music/Chill_Lofi_Session.mp3", dateAdded = "2 days ago"),
      FileItem("8", "Sunset_Mountain_HDR.jpg", "6.7 MB", FileType.PHOTO, "/storage/emulated/0/Pictures/Sunset_Mountain_HDR.jpg", dateAdded = "3 days ago"),
      FileItem("9", "Financial_Report_2026.xlsx", "1.5 MB", FileType.DOCUMENT, "/storage/emulated/0/Documents/Financial_Report_2026.xlsx", dateAdded = "4 days ago"),
      FileItem("10", "SpeedTest_App.apk", "15.0 MB", FileType.APP, "/data/app/com.speedtest.apk", dateAdded = "Today")
    )
  )
  val files: StateFlow<List<FileItem>> = _files.asStateFlow()

  private val _nearbyDevices = MutableStateFlow<List<NearbyDevice>>(
    listOf(
      NearbyDevice("d1", "Apex Pro 5G", "Phone", "192.168.43.101", 98),
      NearbyDevice("d2", "Pixel Tablet X", "Tablet", "192.168.43.105", 85),
      NearbyDevice("d3", "CyberPhone Ultra", "Phone", "192.168.43.112", 92)
    )
  )
  val nearbyDevices: StateFlow<List<NearbyDevice>> = _nearbyDevices.asStateFlow()

  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage("m1", "Apex Pro 5G", "Hey! Ready for high-speed transfer up to 100 MB/s?", "10:30 AM", false),
      ChatMessage("m2", "You", "Turbo Boost enabled! Hitting 100 MB/s now.", "10:31 AM", true)
    )
  )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  private val _transferProgress = MutableStateFlow(0f)
  val transferProgress: StateFlow<Float> = _transferProgress.asStateFlow()

  private val _transferSpeed = MutableStateFlow("0 MB/s")
  val transferSpeed: StateFlow<String> = _transferSpeed.asStateFlow()

  private val _isTransferring = MutableStateFlow(false)
  val isTransferring: StateFlow<Boolean> = _isTransferring.asStateFlow()

  private val _turboBoostEnabled = MutableStateFlow(true)
  val turboBoostEnabled: StateFlow<Boolean> = _turboBoostEnabled.asStateFlow()

  private var transferJob: Job? = null

  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun toggleFileSelection(fileId: String) {
    _files.update { list ->
      list.map { if (it.id == fileId) it.copy(isSelected = !it.isSelected) else it }
    }
  }

  fun selectAllFiles(selected: Boolean) {
    _files.update { list ->
      list.map { it.copy(isSelected = selected) }
    }
  }

  fun toggleTurboBoost(enabled: Boolean) {
    _turboBoostEnabled.value = enabled
  }

  fun startTransfer(targetDevice: NearbyDevice) {
    _currentScreen.value = AppScreen.TransferProgress
    _isTransferring.value = true
    _transferProgress.value = 0f
    _transferSpeed.value = if (_turboBoostEnabled.value) "98.5 MB/s" else "45.2 MB/s"

    transferJob?.cancel()
    transferJob = viewModelScope.launch {
      var progress = 0f
      while (progress < 1.0f) {
        delay(80)
        progress += if (_turboBoostEnabled.value) 0.08f else 0.03f
        if (progress > 1.0f) progress = 1.0f
        _transferProgress.value = progress

        val speedVal = if (_turboBoostEnabled.value) {
          92.0 + (Math.random() * 8.0) // 92 - 100 MB/s peak burst
        } else {
          40.0 + (Math.random() * 15.0)
        }
        _transferSpeed.value = String.format("%.1f MB/s", speedVal)
      }
      _isTransferring.value = false
      _transferSpeed.value = if (_turboBoostEnabled.value) "Complete (99.8 MB/s peak)" else "Complete (48.0 MB/s)"
    }
  }

  fun cancelTransfer() {
    transferJob?.cancel()
    _isTransferring.value = false
    _transferProgress.value = 0f
    _transferSpeed.value = "Cancelled"
  }

  fun sendMessage(text: String) {
    if (text.isBlank()) return
    val newMsg = ChatMessage(
      id = System.currentTimeMillis().toString(),
      sender = "You",
      text = text,
      timestamp = "Just now",
      isMe = true
    )
    _chatMessages.update { it + newMsg }
  }
}
