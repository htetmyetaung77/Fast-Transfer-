package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(viewModel: FlashShareViewModel) {
  val messages by viewModel.chatMessages.collectAsState()
  var inputMessage by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Group Chat & Broadcast", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateTo(AppScreen.Home) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    bottomBar = {
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            placeholder = { Text("Type broadcast message...") },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            )
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = {
              if (inputMessage.isNotBlank()) {
                viewModel.sendMessage(inputMessage)
                inputMessage = ""
              }
            },
            modifier = Modifier.testTag("send_chat_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
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
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        items(messages, key = { it.id }) { msg ->
          ChatBubble(message = msg)
        }
      }
    }
  }
}

@Composable
fun ChatBubble(message: ChatMessage) {
  val alignment = if (message.isMe) Alignment.End else Alignment.Start
  val bubbleColor = if (message.isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
  val textColor = if (message.isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = alignment
  ) {
    Text(message.sender, fontSize = 10.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(modifier = Modifier.height(2.dp))
    Surface(
      color = bubbleColor,
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.widthIn(max = 280.dp)
    ) {
      Column(modifier = Modifier.padding(10.dp)) {
        Text(message.text, color = textColor, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(message.timestamp, fontSize = 9.dp.value.sp, color = textColor.copy(alpha = 0.7f), modifier = Modifier.align(Alignment.End))
      }
    }
  }
}
