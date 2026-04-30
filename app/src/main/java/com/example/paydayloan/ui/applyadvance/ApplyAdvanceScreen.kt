package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.components.AppNavigationBar
import java.util.Locale
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.paydayloan.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.paydayloan.ui.dashboard.DashboardViewModel
import com.example.paydayloan.ui.dashboard.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyAdvanceScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = viewModel(),
    loanViewModel: LoanViewModel = viewModel()
) {
    val dashboardState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
    val loanState by loanViewModel.uiState.collectAsStateWithLifecycle()

    var requestedAmount by remember { mutableStateOf("20000") }
    var expanded by remember { mutableStateOf(false) }
    val purposes = listOf("Short of cash", "Medical emergency", "Family support", "Education")
    var selectedPurpose by remember { mutableStateOf(purposes[0]) }

    LaunchedEffect(Unit) {
        dashboardViewModel.loadDashboard(1L)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Apply for Advance",
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
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(BackgroundBlue)) {
            when (val state = dashboardState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DashboardUiState.Error -> {
                    Text(state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
                is DashboardUiState.Success -> {
                    val data = state.data
                    val amount = requestedAmount.toDoubleOrNull() ?: 0.0
                    val maxEligible = data.eligibleAmount
                    val availableLimit = data.availableLimit

                    // For now, we use local calculation, but we could call loanViewModel.simulateLoan
                    val serviceCharge = if (amount > 0) maxOf(amount * 0.02, 200.0) else 0.0
                    val netAmount = if (amount > 0) amount - serviceCharge else 0.0

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Your Eligibility Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("Your Eligibility", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                                Spacer(modifier = Modifier.height(16.dp))
                                EligibilityRow("Monthly Salary", data.monthlySalary)
                                EligibilityRow("Maximum Limit (80%)", maxEligible)
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BackgroundBlue)
                                EligibilityRow("Available to Withdraw", availableLimit, isHighlight = true)
                            }
                        }

                        // Enter Requested Amount
                        Column {
                            Text(
                                "Requested Amount",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = requestedAmount,
                                onValueChange = { requestedAmount = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = { Text("৳", style = MaterialTheme.typography.titleLarge, color = PrimaryBlue) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color.Transparent
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Min ৳1,000 • Max ৳${String.format(Locale.US, "%,.0f", availableLimit)}",
                                fontSize = 12.sp,
                                color = TextGray
                            )
                        }

                        // Purpose Dropdown
                        Column {
                            Text(
                                "Select Purpose",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box {
                                OutlinedTextField(
                                    value = selectedPurpose,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            "Dropdown",
                                            Modifier.clickable { expanded = true },
                                            tint = PrimaryBlue
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.White,
                                        focusedContainerColor = Color.White,
                                        unfocusedBorderColor = Color.Transparent
                                    )
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.fillMaxWidth(0.9f).background(Color.White)
                                ) {
                                    purposes.forEach { purpose ->
                                        DropdownMenuItem(
                                            text = { Text(purpose) },
                                            onClick = {
                                                selectedPurpose = purpose
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Estimated Charges Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.05f))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    "Summary & Charges",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                ChargeRow("Requested Amount", amount)
                                ChargeRow(
                                    label = "Service Charge (2%)",
                                    amount = serviceCharge,
                                    hasInfo = true
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Net Disbursement",
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlue
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

                        // Repayment Info
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Shield,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Secure repayment from salary on 30 May 2024",
                                    fontSize = 12.sp,
                                    color = TextGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val encodedPurpose = URLEncoder.encode(selectedPurpose, StandardCharsets.UTF_8.toString())
                                navController.navigate("loan_summary/$amount/$serviceCharge/$netAmount/$encodedPurpose")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(16.dp),
                            enabled = amount >= 1000 && amount <= availableLimit
                        ) {
                            Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EligibilityRow(label: String, amount: Double, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = if (isHighlight) PrimaryBlue else TextGray,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            "৳ ${String.format(Locale.US, "%,.0f", amount)}",
            color = if (isHighlight) PrimaryBlue else TextDark,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun ChargeRow(label: String, amount: Double, hasInfo: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = TextGray, fontSize = 14.sp)
            if (hasInfo) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = TextGray
                )
            }
        }
        Text(
            "৳ ${String.format(Locale.US, "%,.0f", amount)}",
            color = TextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
