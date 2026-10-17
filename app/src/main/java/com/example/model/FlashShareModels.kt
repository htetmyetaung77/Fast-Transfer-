package com.example.model

enum class FileType {
  APP, VIDEO, MUSIC, PHOTO, DOCUMENT
}

data class FileItem(
  val id: String,
  val name: String,
  val size: String,
  val type: FileType,
  val path: String,
  val isSelected: Boolean = false,
  val dateAdded: String = "Today",
  val packageSize: Long = 0L
)

data class NearbyDevice(
  val id: String,
  val name: String,
  val deviceType: String,
  val ipAddress: String,
  val signalStrength: Int = 95
)

data class ChatMessage(
  val id: String,
  val sender: String,
  val text: String,
  val timestamp: String,
  val isMe: Boolean
)
