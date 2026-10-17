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
import com.example.model.NearbyDevice
import com.example.viewmodel.AppScreen
import com.example.viewmodel.FlashShareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendReceiveScreen(viewModel: FlashShareViewModel) {
  val devices by viewModel.nearbyDevices.collectAsState()
  var isScanning by remember { mutableStateOf(true) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Radar & P2P Connection", fontWeight = FontWeight.Bold) },
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
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Radar Animation Circle placeholder
      Box(
        modifier = Modifier
          .size(180.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Radar, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
      Text(if (isScanning) "Searching for nearby devices..." else "Scan complete", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
      Text("Ensure Wi-Fi & Hotspot are enabled", fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

      Spacer(modifier = Modifier.height(24.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Nearby Devices Found", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        TextButton(onClick = { isScanning = !isScanning }) {
          Text(if (isScanning) "Pause" else "Scan Again", color = MaterialTheme.colorScheme.primary)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(devices, key = { it.id }) { device ->
          DeviceRow(device = device, onConnect = {
            viewModel.startTransfer(device)
          })
        }
      }
    }
  }
}

@Composable
fun DeviceRow(device: NearbyDevice, onConnect: () -> Unit) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onConnect() }
      .testTag("device_item_${device.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(Color(0xFF00E5FF).copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(24.dp))
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(device.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(2.dp))
        Text("IP: ${device.ipAddress} • Signal: ${device.signalStrength}%", fontSize = 12.dp.value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Button(
        onClick = onConnect,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.testTag("connect_button_${device.id}")
      ) {
        Text("Connect & Send", color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp)
      }
    }
  }
}
