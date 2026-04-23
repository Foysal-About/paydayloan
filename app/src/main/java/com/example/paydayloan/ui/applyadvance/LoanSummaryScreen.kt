package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanSummaryScreen(
    navController: NavController,
    requestedAmount: Double,
    serviceCharge: Double,
    netAmount: Double,
    purpose: String
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Loan Summary",
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Requested Amount",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        "৳ ${String.format(Locale.US, "%,.2f", requestedAmount)}",
                        color = Color(0xFF00695C),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp)
                    
                    SummaryRow("Monthly Salary", 30000.0) // Dummy
                    SummaryRow("Eligible Limit (80%)", 24000.0) // Dummy
                    SummaryRow("Requested Amount", requestedAmount)
                    SummaryRow("Service Charge (2% or 200)", serviceCharge)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "You Will Receive",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00695C),
                            fontSize = 16.sp
                        )
                        Text(
                            "৳ ${String.format(Locale.US, "%,.2f", netAmount)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00695C),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailInfoRow("Repayment Date", "30 May 2024")
                    DetailInfoRow("Repayment Source", "Salary Account")
                    DetailInfoRow("Purpose", purpose)
                }
            }

            // Info Warning Box
            Surface(
                color = Color(0xFFFFF3E0),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Your request will be sent to your employer for approval. After approval, amount will be disbursed to your account.",
                        fontSize = 13.sp,
                        color = Color(0xFFE65100),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    navController.navigate("loan_status")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Submit Request", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = buildAnnotatedString {
                    append("By tapping Submit, you agree to the ")
                    withStyle(style = SpanStyle(color = Color(0xFF00695C), fontWeight = FontWeight.Bold)) {
                        append("terms and\nconditions")
                    }
                    append(" of Pay Day Loan.")
                },
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SummaryRow(label: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(
            "৳ ${String.format(Locale.US, "%,.2f", amount)}",
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(
            value,
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f)
        )
    }
}
