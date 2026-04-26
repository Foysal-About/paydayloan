package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.Loan
import com.example.paydayloan.LoanStatus
import java.util.Locale

import com.example.paydayloan.ui.theme.*
import com.example.paydayloan.ui.components.AppNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStatusScreen(navController: NavController) {
    val loan = Loan(
        id = "LOAN002",
        amount = 20000.0,
        serviceCharge = 400.0,
        netAmount = 19600.0,
        status = LoanStatus.PENDING_EMPLOYER_APPROVAL,
        repaymentDate = "30 May 2024",
        purpose = "Short of cash / Personal need",
        requestDate = "18 May 2024, 10:30 AM"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Loan Status",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundBlue)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Pending Approval",
                            color = WarningOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Waiting for your employer to approve the request.",
                            color = TextGray,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = BackgroundBlue,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "ID: ${loan.id}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextGray
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(WarningOrange.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = WarningOrange,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Request Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Transaction Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    StatusDetailRow("Requested Amount", "৳ ${String.format(Locale.US, "%,.0f", loan.amount)}")
                    StatusDetailRow("Service Charge", "৳ ${String.format(Locale.US, "%,.0f", loan.serviceCharge)}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundBlue)
                    StatusDetailRow(
                        "Net Disbursement", 
                        "৳ ${String.format(Locale.US, "%,.0f", loan.netAmount)}", 
                        isHighlight = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusDetailRow("Expected Repayment", loan.repaymentDate)
                }
            }

            // Status Timeline Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Tracking", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    TimelineItem(
                        title = "Request Submitted",
                        subtitle = loan.requestDate,
                        isFirst = true,
                        isLast = false,
                        status = TimelineStatus.COMPLETED
                    )
                    TimelineItem(
                        title = "Employer Approval",
                        subtitle = "Processing...",
                        isFirst = false,
                        isLast = false,
                        status = TimelineStatus.PENDING
                    )
                    TimelineItem(
                        title = "Disbursement",
                        subtitle = "Pending",
                        isFirst = false,
                        isLast = false,
                        status = TimelineStatus.PENDING
                    )
                    TimelineItem(
                        title = "Salary Repayment",
                        subtitle = "Pending",
                        isFirst = false,
                        isLast = true,
                        status = TimelineStatus.PENDING
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel Button
            TextButton(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Cancel Request", color = Color.Red.copy(alpha = 0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

enum class TimelineStatus {
    COMPLETED, PENDING
}

@Composable
fun StatusDetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextGray, fontSize = 14.sp)
        Text(
            value,
            color = if (isHighlight) PrimaryBlue else TextDark,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun TimelineItem(
    title: String,
    subtitle: String,
    isFirst: Boolean,
    isLast: Boolean,
    status: TimelineStatus
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (status == TimelineStatus.COMPLETED) PrimaryBlue else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (status == TimelineStatus.COMPLETED) PrimaryBlue else Color(0xFFD1D5DB),
                        shape = CircleShape
                    )
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (status == TimelineStatus.COMPLETED) PrimaryBlue.copy(alpha = 0.3f) else Color(0xFFD1D5DB))
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 20.dp)) {
            Text(
                title,
                fontWeight = if (status == TimelineStatus.COMPLETED) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                color = if (status == TimelineStatus.COMPLETED) TextDark else TextGray
            )
            Text(
                subtitle,
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}
