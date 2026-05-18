package com.example.paydayloan.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.theme.*

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

enum class NotificationType {
    APPROVAL, DISBURSEMENT, INFO, REMINDER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    val dummyNotifications = listOf(
        NotificationItem(
            1,
            "Request Approved",
            "Your loan request of ৳20,000 has been approved by HR Manager, Mr. Tanvir Ahmed.",
            "2 hours ago",
            NotificationType.APPROVAL
        ),
        NotificationItem(
            2,
            "Amount Disbursed",
            "Great news! Your advance salary of ৳19,600 has been credited to your City Bank account ending in 4567.",
            "5 hours ago",
            NotificationType.DISBURSEMENT
        ),
        NotificationItem(
            3,
            "Repayment Reminder",
            "Friendly reminder: Your upcoming salary advance repayment of ৳20,000 is scheduled for 30 May 2024.",
            "Yesterday",
            NotificationType.REMINDER
        ),
        NotificationItem(
            4,
            "Policy Update",
            "City Bank has updated the service charge policy for PayDay loans. Please check the 'FAQs' section for details.",
            "2 days ago",
            NotificationType.INFO,
            isRead = true
        ),
        NotificationItem(
            5,
            "Documents Verified",
            "Your employment documents have been successfully verified by our system.",
            "3 days ago",
            NotificationType.APPROVAL,
            isRead = true
        )
    )

    Scaffold(
        containerColor = CityBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CityTextDark
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CityTextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dummyNotifications) { notification ->
                NotificationCard(notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    val icon = when (notification.type) {
        NotificationType.APPROVAL -> Icons.Default.CheckCircle
        NotificationType.DISBURSEMENT -> Icons.Default.Payments
        NotificationType.REMINDER -> Icons.Default.Notifications
        NotificationType.INFO -> Icons.Default.Info
    }

    val iconColor = when (notification.type) {
        NotificationType.APPROVAL -> CitySuccess
        NotificationType.DISBURSEMENT -> CityMaroon
        NotificationType.REMINDER -> CityWarning
        NotificationType.INFO -> Color(0xFF1976D2)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.isRead) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        notification.title,
                        fontWeight = if (notification.isRead) FontWeight.SemiBold else FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CityTextDark
                    )
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(CityMaroon, CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    notification.message,
                    fontSize = 13.sp,
                    color = CityTextGray,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    notification.time,
                    fontSize = 11.sp,
                    color = CityTextGray.copy(alpha = 0.7f)
                )
            }
        }
    }
}
