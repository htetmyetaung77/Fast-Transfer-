package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferProgressScreen(viewModel: FlashShareViewModel) {
  val progress by viewModel.transferProgress.collectAsState()
  val speed by viewModel.transferSpeed.collectAsState()
  val isTransferring by viewModel.isTransferring.collectAsState()
  val turboBoostEnabled by viewModel.turboBoostEnabled.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("High-Speed 100 MB/s Transfer", fontWeight = FontWeight.Bold) },
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
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Speed Indicator Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .background(if (turboBoostEnabled) Color(0xFF00E5FF).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              if (turboBoostEnabled) Icons.Default.Bolt else Icons.Default.Speed,
              contentDescription = null,
              tint = if (turboBoostEnabled) Color(0xFF00E5FF) else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(40.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(speed, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.height(4.dp))
          Text(if (turboBoostEnabled) "Turbo 100 MB/s Burst Mode Active" else "Standard Transfer Mode", fontSize = 12.dp.value.sp, color = if (turboBoostEnabled) Color(0xFF00E676) else MaterialTheme.colorScheme.onSurfaceVariant)

          Spacer(modifier = Modifier.height(20.dp))

          // Turbo Boost Switch
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
              .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Turbo Boost (100 MB/s)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Switch(
              checked = turboBoostEnabled,
              onCheckedChange = { viewModel.toggleTurboBoost(it) },
              colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier.testTag("turbo_boost_switch")
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp)
              .clip(RoundedCornerShape(6.dp))
              .testTag("transfer_progress_bar"),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
          )

          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("${(progress * 100).toInt()}% completed", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Text(if (isTransferring) "Transferring..." else "Finished", color = if (isTransferring) MaterialTheme.colorScheme.secondary else Color(0xFF00E676), fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedButton(
          onClick = { viewModel.cancelTransfer() },
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .testTag("cancel_transfer_button"),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252))
        ) {
          Icon(Icons.Default.Close, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Cancel")
        }

        Button(
          onClick = { viewModel.navigateTo(AppScreen.Home) },
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .testTag("finish_transfer_button"),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Icon(Icons.Default.Done, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Home", color = MaterialTheme.colorScheme.onPrimary)
        }
      }
    }
  }
}
