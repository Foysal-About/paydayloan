package com.example.paydayloan.ui.settings

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.outlined.Logout
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

// Data-driven settings model so a single search field can filter across every row.
private sealed interface SettingsEntry {
    val icon: ImageVector
    val title: String
}

private data class NavEntry(
    override val icon: ImageVector,
    override val title: String,
    val subtitle: String? = null,
    val value: String? = null,
    val badge: String? = null,
    val route: String? = null,
    val onClick: (() -> Unit)? = null
) : SettingsEntry

enum class AppearanceMode(val label: String) {
    SYSTEM("System / Auto"),
    LIGHT("Light Mode"),
    DARK("Dark Mode")
}

private data class ToggleEntry(
    override val icon: ImageVector,
    override val title: String,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
) : SettingsEntry

private data class SettingsSection(val title: String, val entries: List<SettingsEntry>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var appearanceMode by remember {
        mutableStateOf(
            when (ThemeController.mode) {
                ThemeMode.SYSTEM -> AppearanceMode.SYSTEM
                ThemeMode.LIGHT -> AppearanceMode.LIGHT
                ThemeMode.DARK -> AppearanceMode.DARK
            }
        )
    }
    var showAppearanceSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val sections = listOf(
        SettingsSection(
            "Account",
            listOf(
                NavEntry(Icons.Outlined.AccountBalance, "Linked Bank Account", subtitle = "City Bank • **** 4567"),
                NavEntry(Icons.Outlined.Payments, "Repayment Method", subtitle = "Auto-debit from salary")
            )
        ),
        SettingsSection(
            "Security",
            listOf(
                ToggleEntry(Icons.Outlined.Fingerprint, "Biometric Login", biometricEnabled) { biometricEnabled = it },
                NavEntry(Icons.Outlined.Password, "Change Password"),
                NavEntry(Icons.Outlined.Devices, "Linked Devices", badge = "3", route = "linked_devices")
            )
        ),
        SettingsSection(
            "Preferences",
            listOf(
                ToggleEntry(Icons.Outlined.NotificationsNone, "Push Notifications", notificationsEnabled) { notificationsEnabled = it },
                NavEntry(Icons.Outlined.Contrast, "Appearance", value = appearanceMode.label, onClick = { showAppearanceSheet = true }),
                NavEntry(Icons.Outlined.Language, "Language", value = "English (US)")
            )
        ),
        SettingsSection(
            "More",
            listOf(
                NavEntry(Icons.AutoMirrored.Outlined.Article, "Terms & Conditions"),
                NavEntry(Icons.Outlined.Shield, "Privacy Policy"),
                NavEntry(Icons.Outlined.Info, "About City PayDay")
            )
        )
    )

    // Filter each section by title; drop sections that end up empty while searching.
    val visibleSections = sections.mapNotNull { section ->
        val matches =
            if (query.isBlank()) section.entries
            else section.entries.filter { it.title.contains(query, ignoreCase = true) }
        if (matches.isEmpty()) null else SettingsSection(section.title, matches)
    }

    Scaffold(
        containerColor = appColors.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = appColors.textPrimary
                        )
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            SettingsSearchBar(query = query, onQueryChange = { query = it })

            // Profile + Log out — hidden while searching to keep results focused.
            if (query.isBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = appColors.surface,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with a red camera badge for changing the photo.
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(appColors.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.Person, contentDescription = null, tint = appColors.textSecondary, modifier = Modifier.size(40.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(appColors.surface)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(CityMaroon.copy(alpha = 0.12f))
                                    .clickable { /* Change photo */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.PhotoCamera, contentDescription = "Change photo", tint = CityMaroon, modifier = Modifier.size(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Syed Foysal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = appColors.textPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = "Edit profile",
                                    tint = CityMaroon,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { /* Edit Profile */ }
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Software Engineer", color = appColors.textSecondary, fontSize = 14.sp)
                        }
                    }
                }

                // Log out card — red label + icon, centered, matching the reference.
                Surface(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = appColors.surface,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Log out", color = CityMaroon, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(20.dp))
                    }
                }
            }

            visibleSections.forEach { section ->
                SettingsSectionTitle(section.title)
                SettingsCard {
                    section.entries.forEachIndexed { index, entry ->
                        if (index > 0) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = appColors.divider)
                        }
                        when (entry) {
                            is ToggleEntry -> SettingsToggleItem(entry)
                            is NavEntry -> SettingsItem(entry) {
                                when {
                                    entry.onClick != null -> entry.onClick.invoke()
                                    entry.route != null -> navController.navigate(entry.route)
                                }
                            }
                        }
                    }
                }
            }

            if (visibleSections.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No settings match \"$query\"", color = appColors.textSecondary, fontSize = 14.sp)
                }
            }

            // Version Info
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                Text("Version 1.0.2 (Build 45)", color = appColors.textSecondary.copy(alpha = 0.6f), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        if (showAppearanceSheet) {
            AppearanceBottomSheet(
                selected = appearanceMode,
                onSelect = {
                    appearanceMode = it
                    ThemeController.mode = when (it) {
                        AppearanceMode.SYSTEM -> ThemeMode.SYSTEM
                        AppearanceMode.LIGHT -> ThemeMode.LIGHT
                        AppearanceMode.DARK -> ThemeMode.DARK
                    }
                    showAppearanceSheet = false
                },
                onDismiss = { showAppearanceSheet = false }
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Log out", fontWeight = FontWeight.Bold, color = appColors.textPrimary) },
                text = { Text("Are you sure you want to log out of your account?", color = appColors.textSecondary) },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Text("Log out", color = CityMaroon, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel", color = appColors.textSecondary)
                    }
                },
                containerColor = appColors.surface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceBottomSheet(
    selected: AppearanceMode,
    onSelect: (AppearanceMode) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = appColors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Appearance",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            AppearanceMode.entries.forEach { mode ->
                val isSelected = mode == selected
                val label = if (mode == AppearanceMode.SYSTEM) {
                    "System / Auto (Recommended Default)"
                } else {
                    mode.label
                }
                Surface(
                    onClick = { onSelect(mode) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) CityMaroon.copy(alpha = 0.06f) else appColors.background,
                    border = if (isSelected) BorderStroke(1.5.dp, CityMaroon) else null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            label,
                            modifier = Modifier.weight(1f),
                            color = if (isSelected) CityMaroon else appColors.textPrimary,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                        if (isSelected) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search settings", color = appColors.textSecondary) },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = appColors.textSecondary) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Outlined.Close, contentDescription = "Clear", tint = appColors.textSecondary)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = appColors.surface,
            unfocusedContainerColor = appColors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = CityMaroon,
            focusedTextColor = appColors.textPrimary,
            unfocusedTextColor = appColors.textPrimary
        )
    )
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = appColors.textSecondary,
            letterSpacing = 0.3.sp
        ),
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
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
private fun SettingsItem(entry: NavEntry, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(entry.icon, contentDescription = null, tint = appColors.textPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(entry.title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = appColors.textPrimary)
            if (entry.subtitle != null) {
                Text(entry.subtitle, fontSize = 12.sp, color = appColors.textSecondary)
            }
        }
        // Right-aligned value (e.g. "English (US)") when present.
        if (entry.value != null) {
            Text(entry.value, fontSize = 13.sp, color = appColors.textSecondary, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(8.dp))
        }
        // Count badge (e.g. Linked Devices • 3).
        if (entry.badge != null) {
            Box(
                modifier = Modifier
                    .background(CityMaroon.copy(alpha = 0.12f), CircleShape)
                    .defaultMinSize(minWidth = 22.dp, minHeight = 22.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    entry.badge,
                    color = CityMaroon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = appColors.textSecondary.copy(alpha = 0.3f))
    }
}

@Composable
private fun SettingsToggleItem(entry: ToggleEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(entry.icon, contentDescription = null, tint = appColors.textPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(entry.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium, fontSize = 15.sp, color = appColors.textPrimary)
        Text(
            if (entry.checked) "On" else "Off",
            color = if (entry.checked) CityMaroon else appColors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = entry.checked,
            onCheckedChange = entry.onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CityMaroon,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = appColors.textSecondary.copy(alpha = 0.3f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}