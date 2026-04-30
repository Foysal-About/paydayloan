package com.example.paydayloan.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale
import com.example.paydayloan.ui.components.AppNavigationBar
import com.example.paydayloan.api.model.EmployeeDashboardDTO
import com.example.paydayloan.api.model.LoanRequestDTO
import com.example.paydayloan.api.model.ActiveLoanDTO
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.paydayloan.ui.theme.PrimaryBlue
import com.example.paydayloan.ui.theme.SecondaryBlue
import com.example.paydayloan.ui.theme.TextDark
import com.example.paydayloan.ui.theme.TextGray
import com.example.paydayloan.ui.theme.BackgroundBlue
import com.example.paydayloan.ui.theme.WarningOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard(1L) // Hardcoded employee ID for now
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pay Day",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryBlue)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = TextGray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundBlue)
        ) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DashboardUiState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center).padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }
                is DashboardUiState.Success -> {
                    val data = state.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(10.dp)) }

                        // Welcome text
                        item {
                            Column {
                                Text(
                                    "Hello!",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                )
                                Text(
                                    "Track your salary advance and limits",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextGray
                                )
                            }
                        }

                        // Main Eligibility Card
                        item {
                            EligibilityCard(data.eligibleAmount)
                        }

                        // Salary and Limit Info Cards
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                InfoCard(
                                    title = "Monthly Salary",
                                    amount = data.monthlySalary,
                                    icon = Icons.Default.Payments,
                                    modifier = Modifier.weight(1f)
                                )
                                InfoCard(
                                    title = "Available Limit",
                                    amount = data.availableLimit,
                                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Active Loan Card
                        item {
                            ActiveLoanCardFromDTO(data.activeLoan)
                        }

                        // Recent Loans Section
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Recent History",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                TextButton(onClick = { /* TODO */ }) {
                                    Text("View All", color = PrimaryBlue)
                                }
                            }
                        }

                        items(data.loanHistory.take(2)) { loan ->
                            RecentLoanItemFromDTO(loan)
                        }

                        item {
                            Button(
                                onClick = { navController.navigate("apply_advance") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(16.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                            ) {
                                Text("Apply for Advance", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveLoanCardFromDTO(activeLoan: ActiveLoanDTO?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Active Loan Status", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                Spacer(modifier = Modifier.height(8.dp))
                if (activeLoan == null) {
                    Text("No ongoing loans", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Apply today for an instant advance.", color = TextGray, fontSize = 12.sp)
                } else {
                    Text("৳ ${activeLoan.sanctionedAmount}", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                    Text("Repayment: ${activeLoan.maturityDate}", color = TextGray, fontSize = 12.sp)
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = TextGray.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun RecentLoanItemFromDTO(loan: LoanRequestDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        loan.purpose ?: "Salary Advance",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDark
                    )
                    Text(loan.requestDate ?: "", color = TextGray, fontSize = 12.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "৳ ${String.format(Locale.US, "%,.0f", loan.requestedAmount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Text(
                    loan.status ?: "PENDING",
                    color = if (loan.status == "REPAID" || loan.status == "DISBURSED") Color(0xFF2E7D32) else WarningOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EligibilityCard(amount: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PrimaryBlue, SecondaryBlue)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "Eligible for Advance",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "৳ ${String.format(Locale.US, "%,.0f", amount)}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White.copy(alpha = 0.2f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.8f },
                    modifier = Modifier.fillMaxWidth().height(6.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(3.dp)),
                    color = Color.White,
                    trackColor = Color.Transparent,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "80% of your total salary is available",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun InfoCard(title: String, amount: Double, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "৳ ${String.format(Locale.US, "%,.0f", amount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
        }
    }
}
