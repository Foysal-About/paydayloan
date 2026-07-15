package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanStatusScreen(navController: NavController) {
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    "Cancel Request",
                    fontWeight = FontWeight.Bold,
                    color = CityTextDark
                )
            },
            text = {
                Text(
                    "Are you sure you want to cancel this loan request?",
                    color = CityTextGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Yes, Cancel", color = CityMaroon, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("No", color = CityTextGray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        containerColor = CityBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Loan Status",
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
        Box(modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Premium Header Status Card with City Bank Maroon branding
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(CityMaroon.copy(alpha = 0.08f), Color.White)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Pending Employer Approval",
                                    color = CityMaroon,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Your request is waiting for approval from your employer.",
                                    color = CityTextDark,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Requested on 18 May 2024, 10:30 AM",
                                    color = CityTextGray,
                                    fontSize = 12.sp
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = CityMaroon.copy(alpha = 0.1f),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = CityMaroon,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Request Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Request Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CityTextDark)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        StatusRow("Requested Amount", "৳ 20,000.00")
                        StatusRow("Service Charge", "৳ 400.00")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CityBackground)
                        StatusRow("You Will Receive", "৳ 19,600.00", isHighlight = true)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        StatusRow("Repayment Date", "30 May 2024")
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Status", color = CityTextGray, fontSize = 14.sp)
                            Surface(
                                color = CityWarning.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Pending Approval",
                                    color = CityWarning,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Status Timeline Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Status Timeline", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CityTextDark)
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        TimelineItem(
                            title = "Request Submitted",
                            subtitle = "18 May 2024, 10:30 AM",
                            isFirst = true,
                            isLast = false,
                            isActive = true,
                            showClock = true
                        )
                        TimelineItem(
                            title = "Employer Approval",
                            subtitle = "Pending",
                            isFirst = false,
                            isLast = false,
                            isActive = false
                        )
                        TimelineItem(
                            title = "Disbursement",
                            subtitle = "Pending",
                            isFirst = false,
                            isLast = false,
                            isActive = false
                        )
                        TimelineItem(
                            title = "Repayment",
                            subtitle = "Pending",
                            isFirst = false,
                            isLast = true,
                            isActive = false
                        )
                    }
                }

                Button(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = CityMaroon
                    ),
                    border = BorderStroke(1.dp, CityMaroon.copy(alpha = 0.3f))
                ) {
                    Text("Cancel Request", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                
                // Extra space at bottom
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatusRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label, 
            color = if (isHighlight) CityMaroon else CityTextGray, 
            fontSize = 14.sp, 
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            value,
            color = if (isHighlight) CityMaroon else CityTextDark,
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
    isActive: Boolean,
    showClock: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            if (showClock) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = CityMaroon,
                    modifier = Modifier.size(24.dp).background(Color.White, CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 4.dp)
                        .background(Color.White, CircleShape)
                        .background(if (isActive) CityMaroon else Color(0xFFE0E0E0), CircleShape)
                )
            }
            
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isActive) CityMaroon else CityTextDark
            )
            Text(
                subtitle,
                fontSize = 12.sp,
                color = CityTextGray
            )
        }
    }
}
