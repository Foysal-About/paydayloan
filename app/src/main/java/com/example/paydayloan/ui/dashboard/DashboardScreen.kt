package com.example.paydayloan.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale
import com.example.paydayloan.api.model.LoanRequestDTO
import com.example.paydayloan.api.model.ActiveLoanDTO
import com.example.paydayloan.api.model.EmployeeDashboardDTO
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.paydayloan.R
import com.example.paydayloan.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    onOpenDrawer: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard(1L) // Hardcoded employee ID for now
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .background(Color.Transparent.copy(alpha = 0.2f), CircleShape)
                            .clickable { onOpenDrawer() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "S",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                    IconButton(onClick = { /* Rewards */ }) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Rewards", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CityMaroon,
                    scrolledContainerColor = CityMaroon,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is DashboardUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CityMaroon)
                }
            }
            is DashboardUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        color = CityError,
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            is DashboardUiState.Success -> {
                val data = state.data
                var cardHeightPx by remember { mutableStateOf(0) }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding())
                ) {
                    item { RedHeaderSection(cardOverlapPx = cardHeightPx / 2) }

                    item {
                        WhiteHomeContainer(
                            data = data,
                            navController = navController,
                            cardHeightPx = cardHeightPx,
                            onCardSizeChanged = { cardHeightPx = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RedHeaderSection(cardOverlapPx: Int) {
    val density = LocalDensity.current
    val cardOverlapDp = with(density) { cardOverlapPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors = listOf(CityMaroon, CityMaroonDark))
            )
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = cardOverlapDp + 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    "Good Evening",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
                Text(
                    "SYED FOYSAL",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun WhiteHomeContainer(
    data: EmployeeDashboardDTO,
    navController: NavController,
    cardHeightPx: Int,
    onCardSizeChanged: (Int) -> Unit
) {
    val density = LocalDensity.current
    val cardOverlapDp = with(density) { (cardHeightPx / 2).toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = cardOverlapDp + 24.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Salary and Limit Info Cards
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

            Button(
                onClick = { navController.navigate("apply_advance") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CityMaroon),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Apply for Advance", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Active Loan Card
            ActiveLoanCardFromDTO(data.activeLoan)

            // Recent Loans Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent History",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CityTextDark
                    )
                )
                TextButton(onClick = { navController.navigate("history") }) {
                    Text("View All", color = CityMaroon, fontWeight = FontWeight.Bold)
                }
            }

            data.loanHistory.take(2).forEach { loan ->
                RecentLoanItemFromDTO(loan)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Floating Account Card: overlaps the red header above and the white container below.
        EligibilityCard(
            amount = data.eligibleAmount,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 20.dp)
                .offset { IntOffset(0, -(cardHeightPx / 2)) }
                .onSizeChanged { onCardSizeChanged(it.height) }
        )
    }
}


@Composable
fun ActiveLoanCardFromDTO(activeLoan: ActiveLoanDTO?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Active Loan Status", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CityTextDark)
                Spacer(modifier = Modifier.height(8.dp))
                if (activeLoan == null) {
                    Text("No ongoing loans", color = CityMaroon, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Apply today for an instant advance.", color = CityTextGray, fontSize = 12.sp)
                } else {
                    Text("৳ ${activeLoan.sanctionedAmount}", color = CityMaroon, fontWeight = FontWeight.Bold)
                    Text("Repayment: ${activeLoan.maturityDate}", color = CityTextGray, fontSize = 12.sp)
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = CityTextGray.copy(alpha = 0.5f),
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
                        .background(CitySuccess.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = CitySuccess, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        loan.purpose ?: "Salary Advance",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CityTextDark
                    )
                    Text(loan.requestDate ?: "", color = CityTextGray, fontSize = 12.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "৳ ${String.format(Locale.US, "%,.0f", loan.requestedAmount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = CityTextDark
                )
                Text(
                    loan.status ?: "PENDING",
                    color = when (loan.status) {
                        "REPAID", "DISBURSED" -> CitySuccess
                        "FAILED", "REJECTED" -> CityError
                        else -> CityWarning
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EligibilityCard(amount: Double, modifier: Modifier = Modifier) {
    var isRevealed by remember { mutableStateOf(false) }
    val cardShape = RoundedCornerShape(24.dp)

    // Using Surface or Modifier.shadow to ensure the glass card floats above everything
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isRevealed) "৳ ${String.format(Locale.US, "%,.2f", amount)}" else "৳XXXX.XX",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CityTextDark
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { isRevealed = !isRevealed },
                        tint = CityTextGray
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.pl),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "PayDay Advance A/C",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CityTextDark
                        )
                        Text(
                            "230446137000",
                            fontSize = 13.sp,
                            color = CityTextGray
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun InfoCard(title: String, amount: Double, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(CityMaroon.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CityMaroon,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                title,
                color = CityTextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "৳ ${String.format(Locale.US, "%,.0f", amount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = CityTextDark
            )
        }
    }
}

