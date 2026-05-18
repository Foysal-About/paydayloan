package com.example.paydayloan.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CityBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CityTextDark
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CityTextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Profile Summary Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(CityMaroon.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Syed Foysal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CityTextDark)
                        Text("Software Engineer", color = CityTextGray, fontSize = 14.sp)
                    }
                    IconButton(onClick = { /* Edit Profile */ }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit", tint = CityMaroon, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Account Settings
            SettingsSectionTitle("Account Settings")
            SettingsCard {
                SettingsItem(Icons.Outlined.AccountBalance, "Linked Bank Account", "City Bank - **** 4567")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = CityBackground)
                SettingsItem(Icons.Outlined.Payments, "Repayment Method", "Auto-debit from salary")
            }

            // Security Settings
            SettingsSectionTitle("Security")
            SettingsCard {
                SettingsToggleItem(Icons.Outlined.Fingerprint, "Biometric Login", biometricEnabled) { biometricEnabled = it }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = CityBackground)
                SettingsItem(Icons.Outlined.Lock, "Change Password")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = CityBackground)
                SettingsItem(Icons.Outlined.VerifiedUser, "Two-Factor Authentication")
            }

            // Preferences
            SettingsSectionTitle("Preferences")
            SettingsCard {
                SettingsToggleItem(Icons.Outlined.NotificationsActive, "Push Notifications", notificationsEnabled) { notificationsEnabled = it }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = CityBackground)
                SettingsItem(Icons.Outlined.Translate, "Language", "English (US)")
            }

            // Legal & Info
            SettingsSectionTitle("More")
            SettingsCard {
                SettingsItem(Icons.Outlined.Info, "About City PayDay")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = CityBackground)
                SettingsItem(Icons.Outlined.GppGood, "Privacy Policy")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = CityBackground)
                SettingsItem(Icons.AutoMirrored.Outlined.Article, "Terms & Conditions")
            }

            // Version Info
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                Text("Version 1.0.2 (Build 45)", color = CityTextGray.copy(alpha = 0.6f), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            color = CityMaroon,
            letterSpacing = 0.5.sp
        ),
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(CityMaroon.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = CityTextDark)
            if (subtitle != null) {
                Text(subtitle, fontSize = 12.sp, color = CityTextGray)
            }
        }
        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = CityTextGray.copy(alpha = 0.3f))
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(CityMaroon.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium, fontSize = 15.sp, color = CityTextDark)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CityMaroon,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = CityTextGray.copy(alpha = 0.3f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}
