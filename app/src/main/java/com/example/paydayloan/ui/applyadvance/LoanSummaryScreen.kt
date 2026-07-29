package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    monthlySalary: Double,
    eligibleAmount: Double,
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
    var termsAccepted by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    termsAccepted = true
                    showTermsDialog = false
                }) {
                    Text("Accept", color = CityMaroon, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Close", color = appColors.textSecondary)
                }
            },
            title = {
                Text("Terms & Conditions", fontWeight = FontWeight.Bold, color = appColors.textPrimary)
            },
            text = {
                Text(
                    text = "1. The advance amount and a 2% service charge will be automatically " +
                        "deducted from your next salary disbursement.\n\n" +
                        "2. This request is subject to approval by your employer and City Bank.\n\n" +
                        "3. You confirm that the information provided is accurate and that you are " +
                        "authorized to request this salary advance.\n\n" +
                        "4. Early repayment does not reduce the service charge already applied.\n\n" +
                        "5. Pay Day Loan may share request details with your employer solely for " +
                        "approval and repayment purposes.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = appColors.textPrimary
                )
            },
            containerColor = appColors.surface
        )
    }

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
                            color = appColors.textPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = appColors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(appColors.background)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            "Requested Amount",
                            color = appColors.textSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "৳ ${String.format(Locale.US, "%,.0f", requestedAmount)}",
                            color = CityMaroon,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = appColors.divider)
                        
                        SummaryRow("Monthly Salary", monthlySalary)
                        SummaryRow("Eligible for Advance", eligibleAmount)
                        SummaryRow("Service Charge (2%)", serviceCharge)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "You Will Receive",
                                fontWeight = FontWeight.Bold,
                                color = CityMaroon,
                                fontSize = 16.sp
                            )
                            Text(
                                "৳ ${String.format(Locale.US, "%,.0f", netAmount)}",
                                fontWeight = FontWeight.ExtraBold,
                                color = CityMaroon,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                // Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.surface),
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
                    color = CityWarning.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = CityWarning,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Your request will be sent to your employer for approval. After approval, the amount will be disbursed to your account.",
                            fontSize = 13.sp,
                            color = appColors.textPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Terms & conditions acceptance (required before submitting)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = CityMaroon,
                            uncheckedColor = appColors.textSecondary
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("I have read and agree to the ")
                            withStyle(style = SpanStyle(color = CityMaroon, fontWeight = FontWeight.Bold)) {
                                append("terms and conditions")
                            }
                            append(" of Pay Day Loan.")
                        },
                        fontSize = 13.sp,
                        color = appColors.textSecondary,
                        lineHeight = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showTermsDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.applyLoan(1L, requestedAmount, decodedPurpose)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CityMaroon,
                        disabledContainerColor = CityMaroon.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = uiState !is LoanUiState.Loading && termsAccepted
                ) {
                    if (uiState is LoanUiState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Submit Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
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
        Text(label, color = appColors.textSecondary, fontSize = 14.sp)
        Text(
            "৳ ${String.format(Locale.US, "%,.0f", amount)}",
            color = appColors.textPrimary,
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
        Text(label, color = appColors.textSecondary, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(
            value,
            color = appColors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f)
        )
    }
}
