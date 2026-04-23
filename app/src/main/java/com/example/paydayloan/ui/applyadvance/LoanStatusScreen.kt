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
                            color = Color(0xFF1A237E)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7F0))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Pending Employer Approval",
                            color = Color(0xFFE67E22),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Your request is waiting for approval from your employer.",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Requested on ${loan.requestDate}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFFE67E22),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Request Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Request Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    StatusDetailRow("Requested Amount", "৳ ${String.format(Locale.US, "%,.2f", loan.amount)}")
                    StatusDetailRow("Service Charge", "৳ ${String.format(Locale.US, "%,.2f", loan.serviceCharge)}")
                    StatusDetailRow(
                        "You Will Receive", 
                        "৳ ${String.format(Locale.US, "%,.2f", loan.netAmount)}", 
                        isGreen = true
                    )
                    StatusDetailRow("Repayment Date", loan.repaymentDate)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Status", color = Color.Gray, fontSize = 14.sp)
                        Surface(
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Pending Approval",
                                color = Color(0xFFE67E22),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Status Timeline Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Status Timeline", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TimelineItem(
                        title = "Request Submitted",
                        subtitle = loan.requestDate,
                        isFirst = true,
                        isLast = false,
                        status = TimelineStatus.COMPLETED
                    )
                    TimelineItem(
                        title = "Employer Approval",
                        subtitle = "Pending",
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
                        title = "Repayment",
                        subtitle = "Pending",
                        isFirst = false,
                        isLast = true,
                        status = TimelineStatus.PENDING
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel Button
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF00695C)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00695C))
            ) {
                Text("Cancel Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

enum class TimelineStatus {
    COMPLETED, PENDING
}

@Composable
fun StatusDetailRow(label: String, value: String, isGreen: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(
            value,
            color = if (isGreen) Color(0xFF00695C) else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isGreen) FontWeight.Bold else FontWeight.Normal
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
                    .size(24.dp)
                    .background(
                        color = if (status == TimelineStatus.COMPLETED) Color(0xFFE67E22) else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (status == TimelineStatus.COMPLETED) Color(0xFFE67E22) else Color(0xFFD1D5DB),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (status == TimelineStatus.COMPLETED) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(Color(0xFFD1D5DB))
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (status == TimelineStatus.COMPLETED) Color(0xFF1A237E) else Color.Gray
            )
            Text(
                subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
