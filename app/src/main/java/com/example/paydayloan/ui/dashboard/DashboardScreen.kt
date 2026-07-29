package com.example.paydayloan.ui.dashboard

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

// Fixed size so the floating account card's overlap is deterministic — no runtime
// height measurement/feedback loop that could settle into a wrong first-frame layout.
private val EligibilityCardHeight = 120.dp
private val EligibilityCardOverlap = EligibilityCardHeight / 2

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
                            .background(Color.Transparent.copy(alpha = 0.2f), CircleShape),
//                            .clickable { onOpenDrawer() },
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
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "Notification",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )                    }
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding())
                ) {
                    item {
                        // Header and container live in ONE item so the floating card
                        // (which overflows above the container) is guaranteed to draw
                        // on top — LazyColumn doesn't promise z-order across items.
                        Column {
                            RedHeaderSection()
                            WhiteHomeContainer(
                                data = data,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RedHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors = listOf(CityMaroon, CityMaroonDark))
            )
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = EligibilityCardOverlap + 24.dp)
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
                        fontWeight = FontWeight.Normal,
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
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(appColors.surface, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = EligibilityCardOverlap + 24.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
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
                        color = appColors.textPrimary
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
                .offset(y = -EligibilityCardOverlap)
        )
    }
}


@Composable
fun ActiveLoanCardFromDTO(activeLoan: ActiveLoanDTO?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Active Loan Status", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = appColors.textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                if (activeLoan == null) {
                    Text("No ongoing loans", color = CityMaroon, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Apply today for an instant advance.", color = appColors.textSecondary, fontSize = 12.sp)
                } else {
                    Text("৳ ${activeLoan.sanctionedAmount}", color = CityMaroon, fontWeight = FontWeight.Bold)
                    Text("Repayment: ${activeLoan.maturityDate}", color = appColors.textSecondary, fontSize = 12.sp)
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = appColors.textSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Maps a loan status to its visual treatment: accent color + leading icon.
// Keeps the item layout declarative and the color/icon logic in one place.
private data class LoanStatusStyle(val color: Color, val icon: ImageVector)

private fun statusStyle(status: String?): LoanStatusStyle = when (status) {
    "REPAID", "DISBURSED" -> LoanStatusStyle(CitySuccess, Icons.Default.CheckCircle)
    "FAILED", "REJECTED" -> LoanStatusStyle(CityError, Icons.Default.Cancel)
    else -> LoanStatusStyle(CityWarning, Icons.Default.Schedule)
}

@Composable
fun RecentLoanItemFromDTO(loan: LoanRequestDTO) {
    val style = statusStyle(loan.status)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status-colored avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(style.color.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    style.icon,
                    contentDescription = null,
                    tint = style.color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title + date take the remaining width so a long purpose can't crowd the amount.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    loan.purpose ?: "Salary Advance",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = appColors.textPrimary,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(loan.requestDate ?: "", color = appColors.textSecondary, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Amount over a tinted status pill, right-aligned.
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "৳ ${String.format(Locale.US, "%,.0f", loan.requestedAmount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = appColors.textPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(style.color.copy(alpha = 0.12f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        loan.status ?: "PENDING",
                        color = style.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EligibilityCard(amount: Double, modifier: Modifier = Modifier) {
    var isRevealed by remember { mutableStateOf(false) }
    val cardShape = RoundedCornerShape(24.dp)

    // Frosted glass: translucent at the top so the red header bleeds through as pink,
    // fading to near-opaque white by mid-card — matches the City Touch account card.
    // Elevated so it unmistakably floats in front of both the header and the container.
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(EligibilityCardHeight),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to appColors.glassBase.copy(alpha = 0.99f),
                        0.45f to appColors.glassBase.copy(alpha = 0.85f),
                        1.0f to appColors.glassBase.copy(alpha = 0.97f)
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isRevealed) "৳ ${String.format(Locale.US, "%,.2f", amount)}" else "৳XXXX.XX",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = appColors.textPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { isRevealed = !isRevealed },
                        tint = appColors.textSecondary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.city_logo),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "PayDay Advance A/C",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = appColors.textPrimary
                        )
                        Text(
                            "230446137000",
                            fontSize = 13.sp,
                            color = appColors.textSecondary
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
        colors = CardDefaults.cardColors(containerColor = appColors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),) {
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
                color = appColors.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "৳ ${String.format(Locale.US, "%,.0f", amount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = appColors.textPrimary
            )
        }
    }
}

