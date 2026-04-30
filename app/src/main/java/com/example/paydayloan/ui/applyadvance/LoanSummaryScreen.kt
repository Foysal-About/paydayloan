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
import com.example.paydayloan.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanSummaryScreen(
    navController: NavController,
    requestedAmount: Double,
    serviceCharge: Double,
    netAmount: Double,
    purpose: String,
    viewModel: LoanViewModel = viewModel()
) {
    val decodedPurpose = remember(purpose) {
        try {
            URLDecoder.decode(purpose, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            purpose
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is LoanUiState.RequestSuccess) {
            navController.navigate("loan_status") {
                popUpTo("dashboard") { inclusive = false }
            }
        } else if (uiState is LoanUiState.Error) {
            snackbarHostState.showSnackbar((uiState as LoanUiState.Error).message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Loan Summary",
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
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(BackgroundBlue)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Requested Amount",
                            color = TextGray,
                            fontSize = 14.sp
                        )
                        Text(
                            "৳ ${String.format(Locale.US, "%,.0f", requestedAmount)}",
                            color = PrimaryBlue,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BackgroundBlue)
                        
                        SummaryRow("Requested Amount", requestedAmount)
                        SummaryRow("Service Charge (2%)", serviceCharge)
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "You Will Receive",
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                fontSize = 16.sp
                            )
                            Text(
                                "৳ ${String.format(Locale.US, "%,.0f", netAmount)}",
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryBlue,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                // Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DetailInfoRow("Repayment Date", "30 May 2024")
                        DetailInfoRow("Repayment Source", "Salary Account")
                        DetailInfoRow("Purpose", decodedPurpose)
                    }
                }

                // Info Warning Box
                Surface(
                    color = WarningOrange.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = WarningOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Your request will be sent to your employer for approval. After approval, the amount will be disbursed to your account.",
                            fontSize = 13.sp,
                            color = WarningOrange,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.applyLoan(1L, requestedAmount, decodedPurpose)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp),
                    enabled = uiState !is LoanUiState.Loading
                ) {
                    if (uiState is LoanUiState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Submit Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = buildAnnotatedString {
                        append("By tapping Submit, you agree to the ")
                        withStyle(style = SpanStyle(color = PrimaryBlue, fontWeight = FontWeight.Bold)) {
                            append("terms and conditions")
                        }
                        append(" of Pay Day Loan.")
                    },
                    fontSize = 12.sp,
                    color = TextGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
        Text(label, color = TextGray, fontSize = 14.sp)
        Text(
            "৳ ${String.format(Locale.US, "%,.0f", amount)}",
            color = TextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
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
        Text(label, color = TextGray, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(
            value,
            color = TextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f)
        )
    }
}
