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
        containerColor = CityBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Help & Support",
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

            // 1. Quick Contact Row
            Text(
                "How can we help you?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = CityTextDark
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
                placeholder = { Text("Search help articles...", color = CityTextGray) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = CityTextGray) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = CityMaroon.copy(alpha = 0.3f)
                )
            )

            // 3. FAQ Categories
            Text(
                "Popular Topics",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CityTextDark
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SupportTopicItem(Icons.Outlined.Payments, "Loan Disbursement", "Issues with receiving funds")
                SupportTopicItem(Icons.Outlined.VerifiedUser, "Identity Verification", "KYC and document updates")
                SupportTopicItem(Icons.Outlined.AccountBalance, "Repayment Methods", "Automatic & manual repayments")
                SupportTopicItem(Icons.Outlined.Security, "Security & Login", "Password and biometric issues")
            }

            // 4. Emergency Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = CityMaroon.copy(alpha = 0.05f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CityMaroon.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(CityMaroon, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.ErrorOutline, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Report Fraud", fontWeight = FontWeight.Bold, color = CityMaroon)
                        Text("Block account immediately", fontSize = 12.sp, color = CityTextGray)
                    }
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = CityMaroon)
                }
            }

            // 5. App Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(CitySuccess, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("All systems are operational", fontSize = 12.sp, color = CityTextGray)
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
        color = Color.White,
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
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CityTextDark)
            Text(subtitle, fontSize = 12.sp, color = CityTextGray)
        }
    }
}

@Composable
fun SupportTopicItem(icon: ImageVector, title: String, subtitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
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
                    .background(CityBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = CityTextDark.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CityTextDark)
                Text(subtitle, fontSize = 12.sp, color = CityTextGray)
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = CityTextGray.copy(alpha = 0.4f))
        }
    }
}
