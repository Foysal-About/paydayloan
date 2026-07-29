package com.example.paydayloan.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.theme.*

private data class LinkedDevice(
    val name: String,
    val details: String,
    val lastActive: String,
    val isThisDevice: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkedDevicesScreen(navController: NavController) {
    val thisDevice = LinkedDevice(
        name = "Pixel 8 Pro",
        details = "Android 14 • Dhaka, BD",
        lastActive = "Active now",
        isThisDevice = true
    )
    val linkedDevices = remember {
        mutableStateListOf(
            LinkedDevice("Samsung S24 Ultra", "Android 14 • Khulna, BD", "Active 2 hours ago"),
            LinkedDevice("iPhone 11 Pro", "iOS 18.2 • Dhaka, BD", "Active yesterday, 9:24 PM")
        )
    }

    Scaffold(
        containerColor = appColors.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Linked Devices",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = appColors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "City PayDay can be opened on these devices. Manage where you're signed in and remove anything you don't recognise.",
                color = appColors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            SettingsGroupLabel("THIS DEVICE")
            DeviceCard {
                DeviceRow(device = thisDevice, onRemove = null)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsGroupLabel("LINKED DEVICES", modifier = Modifier.weight(1f))
                Text(
                    "${linkedDevices.size} active",
                    color = appColors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if (linkedDevices.isEmpty()) {
                DeviceCard {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No other devices signed in.", color = appColors.textSecondary, fontSize = 14.sp)
                    }
                }
            } else {
                DeviceCard {
                    linkedDevices.forEachIndexed { index, device ->
                        if (index > 0) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = appColors.divider)
                        }
                        DeviceRow(device = device, onRemove = { linkedDevices.remove(device) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SettingsGroupLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = appColors.textSecondary,
            letterSpacing = 1.sp
        ),
        modifier = modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun DeviceCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = appColors.surface,
        shadowElevation = 1.dp
    ) {
        Column(content = content)
    }
}

@Composable
private fun DeviceRow(device: LinkedDevice, onRemove: (() -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(CityMaroon.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Smartphone, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(device.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = appColors.textPrimary)
                if (device.isThisDevice) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(CityMaroon.copy(alpha = 0.12f), RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "THIS DEVICE",
                            color = CityMaroon,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(device.details, color = appColors.textSecondary, fontSize = 12.sp)
            Text(device.lastActive, color = appColors.textSecondary.copy(alpha = 0.7f), fontSize = 11.sp)
        }

        if (onRemove != null) {
            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Device options", tint = appColors.textSecondary)
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Remove device", color = CityError) },
                        onClick = {
                            menuExpanded = false
                            onRemove()
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Logout, contentDescription = null, tint = CityError)
                        }
                    )
                }
            }
        }
    }
}