package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    var requestedAmount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val purposes = listOf("Short of cash", "Medical emergency", "Family support", "Education", "Other")
    var selectedPurpose by remember { mutableStateOf(purposes[0]) }
    var customPurpose by remember { mutableStateOf("") }
    val isCustomPurpose = selectedPurpose == "Other"
    // The purpose value actually submitted: the typed text when "Other" is chosen.
    val effectivePurpose = if (isCustomPurpose) customPurpose.trim() else selectedPurpose

    LaunchedEffect(Unit) {
        dashboardViewModel.loadDashboard(1L)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appColors.background)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Apply for Advance",
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
            Box(modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
                when (val state = dashboardState) {
                    is DashboardUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = CityMaroon)
                    }
                    is DashboardUiState.Error -> {
                        Text(state.message, color = CityError, modifier = Modifier.align(Alignment.Center))
                    }
                    is DashboardUiState.Success -> {
                        val data = state.data
                        val amount = requestedAmount.toDoubleOrNull() ?: 0.0
                        // Hard cap: a person can take at most 40% of monthly salary
                        val maxEligible = minOf(data.monthlySalary * 0.40, data.eligibleAmount)
                        val availableLimit = minOf(data.availableLimit, maxEligible)

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
                            GlassCard {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text("Your Eligibility", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = appColors.textPrimary)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    EligibilityRow("Monthly Salary", data.monthlySalary)
                                    EligibilityRow("Maximum Limit (40%)", maxEligible)
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = appColors.divider
                                    )
                                    EligibilityRow("Available to Withdraw", availableLimit, isHighlight = true)
                                }
                            }

                            // Enter Requested Amount
                            Column {
                                Text(
                                    "Requested Amount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = appColors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = requestedAmount,
                                    onValueChange = { input ->
                                        requestedAmount = input.filter { it.isDigit() }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    placeholder = {
                                        Text("Enter the requested amount", color = appColors.textSecondary.copy(alpha = 0.8f))
                                    },
                                    leadingIcon = { Text("৳", style = MaterialTheme.typography.titleLarge, color = CityMaroon) },
                                    isError = amount > availableLimit,
                                    supportingText = if (amount > availableLimit) {
                                        {
                                            Text(
                                                "Amount exceeds your 40% limit of ৳${String.format(Locale.US, "%,.0f", availableLimit)}",
                                                color = CityError
                                            )
                                        }
                                    } else null,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = appColors.glassBase.copy(alpha = 0.55f),
                                        focusedContainerColor = appColors.glassBase.copy(alpha = 0.7f),
                                        focusedBorderColor = CityMaroon,
                                        unfocusedBorderColor = appColors.glassBorder,
                                        errorContainerColor = appColors.glassBase.copy(alpha = 0.55f)
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "Min ৳1,000 • Max ৳${String.format(Locale.US, "%,.0f", availableLimit)} (40% of salary)",
                                    fontSize = 12.sp,
                                    color = appColors.textSecondary
                                )
                            }

                            // Purpose Dropdown
                            Column {
                                Text(
                                    "Select Purpose",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = appColors.textPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = selectedPurpose,
                                        onValueChange = {},
                                        readOnly = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(20.dp),
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.ArrowDropDown,
                                                "Dropdown",
                                                tint = CityMaroon
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedContainerColor = appColors.glassBase.copy(alpha = 0.55f),
                                            focusedContainerColor = appColors.glassBase.copy(alpha = 0.7f),
                                            focusedBorderColor = CityMaroon,
                                            unfocusedBorderColor = if (expanded) CityMaroon else appColors.glassBorder
                                        )
                                    )
                                    // Transparent overlay to catch clicks anywhere on the field
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .clickable { expanded = true }
                                    )
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .background(appColors.surface.copy(alpha = 0.95f))
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

                                // When "Other" is chosen, let the user type their own purpose.
                                if (isCustomPurpose) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = customPurpose,
                                        onValueChange = { customPurpose = it },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(20.dp),
                                        singleLine = true,
                                        placeholder = {
                                            Text("Type your purpose", color = appColors.textSecondary.copy(alpha = 0.8f))
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedContainerColor = appColors.glassBase.copy(alpha = 0.55f),
                                            focusedContainerColor = appColors.glassBase.copy(alpha = 0.7f),
                                            focusedBorderColor = CityMaroon,
                                            unfocusedBorderColor = appColors.glassBorder
                                        )
                                    )
                                }
                            }

                            // Estimated Charges Card
                            GlassCard(borderColor = CityMaroon.copy(alpha = 0.25f)) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        "Summary & Charges",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = appColors.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    ChargeRow("Requested Amount", amount)
                                    ChargeRow(
                                        label = "Service Charge (2%)",
                                        amount = serviceCharge,
                                        hasInfo = true
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = appColors.divider
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Net Disbursement",
                                            fontWeight = FontWeight.Bold,
                                            color = CityMaroon
                                        )
                                        Text(
                                            "৳ ${String.format(Locale.US, "%,.0f", netAmount)}",
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CityMaroon,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }

                            // Repayment Info
                            GlassCard(cornerRadius = 20.dp, borderColor = CitySuccess.copy(alpha = 0.25f)) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.Shield,
                                        contentDescription = null,
                                        tint = CitySuccess,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Secure repayment from salary on 30 May 2024",
                                        fontSize = 12.sp,
                                        color = appColors.textSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val encodedPurpose = URLEncoder.encode(effectivePurpose, StandardCharsets.UTF_8.toString())
                                    navController.navigate("loan_summary/$amount/$serviceCharge/$netAmount/$encodedPurpose/${data.monthlySalary}/${maxEligible}")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CityMaroon,
                                    disabledContainerColor = CityMaroon.copy(alpha = 0.35f)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                enabled = amount >= 1000 && amount <= availableLimit &&
                                    (!isCustomPurpose || effectivePurpose.isNotBlank())
                            ) {
                                Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }

                            // Extra space at bottom
                            Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassCard(
    cornerRadius: androidx.compose.ui.unit.Dp = 20.dp,
    borderColor: Color = appColors.glassBorder,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        appColors.glassBase.copy(alpha = 0.65f),
                        appColors.glassBase.copy(alpha = 0.45f)
                    )
                )
            )
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius)),
        content = content
    )
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
            color = if (isHighlight) CityMaroon else appColors.textSecondary,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            "৳ ${String.format(Locale.US, "%,.0f", amount)}",
            color = if (isHighlight) CityMaroon else appColors.textPrimary,
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
            Text(label, color = appColors.textSecondary, fontSize = 14.sp)
            if (hasInfo) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = appColors.textSecondary
                )
            }
        }
        Text(
            "৳ ${String.format(Locale.US, "%,.0f", amount)}",
            color = appColors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}