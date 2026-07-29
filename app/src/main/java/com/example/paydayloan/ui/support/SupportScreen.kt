package com.example.paydayloan.ui.support

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
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
fun SupportScreen(navController: NavController) {
    Scaffold(
        containerColor = appColors.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Help & Support",
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

            // 1. Quick Contact Row
            Text(
                "How can we help you?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContactCard(
                    icon = Icons.AutoMirrored.Outlined.Chat,
                    title = "Live Chat",
                    subtitle = "Wait time: 2 min",
                    modifier = Modifier.weight(1f)
                )
                ContactCard(
                    icon = Icons.Outlined.Call,
                    title = "Call Support",
                    subtitle = "24/7 Available",
                    modifier = Modifier.weight(1f)
                )
            }

            // 2. Search Box Placeholder
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search help articles...", color = appColors.textSecondary) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = appColors.textSecondary) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = appColors.surface,
                    focusedContainerColor = appColors.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = CityMaroon.copy(alpha = 0.3f)
                )
            )

            // 3. FAQ Categories
            Text(
                "Popular Topics",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SupportTopicItem(Icons.Outlined.Payments, "Loan Disbursement", "Issues with receiving funds")
                SupportTopicItem(Icons.Outlined.VerifiedUser, "Identity Verification", "KYC and document updates")
                SupportTopicItem(Icons.Outlined.AccountBalance, "Repayment Methods", "Automatic & manual repayments")
                SupportTopicItem(Icons.Outlined.Security, "Security & Login", "Password and biometric issues")
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


@Composable
fun ContactCard(icon: ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = appColors.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CityMaroon.copy(alpha = 0.08f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = appColors.textPrimary)
            Text(subtitle, fontSize = 12.sp, color = appColors.textSecondary)
        }
    }
}

@Composable
fun SupportTopicItem(icon: ImageVector, title: String, subtitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = appColors.surface
    ) {
        Row(
            modifier = Modifier
                .clickable { /* Navigate to detail */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(appColors.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = appColors.textPrimary.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = appColors.textPrimary)
                Text(subtitle, fontSize = 12.sp, color = appColors.textSecondary)
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = appColors.textSecondary.copy(alpha = 0.4f))
        }
    }
}
