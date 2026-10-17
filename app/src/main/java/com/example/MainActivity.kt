package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: FlashShareViewModel = viewModel()
        val currentScreen by viewModel.currentScreen.collectAsState()

        if (currentScreen != AppScreen.Home) {
          BackHandler {
            viewModel.navigateTo(AppScreen.Home)
          }
        }

        when (currentScreen) {
          is AppScreen.Home -> HomeScreen(viewModel)
          is AppScreen.SendReceive -> SendReceiveScreen(viewModel)
          is AppScreen.FileManager -> FileManagerScreen(viewModel)
          is AppScreen.AppsShare -> AppsShareScreen(viewModel)
          is AppScreen.MediaHub -> MediaHubScreen(viewModel)
          is AppScreen.GroupChat -> GroupChatScreen(viewModel)
          is AppScreen.TransferProgress -> TransferProgressScreen(viewModel)
        }
      }
    }
  }
}
