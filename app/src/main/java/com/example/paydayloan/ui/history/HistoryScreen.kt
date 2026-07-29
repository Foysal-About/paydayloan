package com.example.paydayloan.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.paydayloan.api.model.LoanRequestDTO
import com.example.paydayloan.ui.dashboard.DashboardUiState
import com.example.paydayloan.ui.dashboard.DashboardViewModel
import com.example.paydayloan.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard(1L)
    }

    val c = appColors
    val brandGradient = if (c.isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3A0A0D),
                Color(0xFF1A0708),
                Color(0xFF0D0D0D)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFBEEEC),
                Color(0xFFFDF8F7),
                Color(0xFFF7F7F7)
            )
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Transaction History",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brandGradient)
                .padding(top = padding.calculateTopPadding())
        ) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = CityMaroon)
                }
                is DashboardUiState.Error -> {
                    Text(
                        text = state.message,
                        color = CityError,
                        modifier = Modifier.align(Alignment.Center).padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }
                is DashboardUiState.Success -> {
                    val history = state.data.loanHistory
                    if (history.isEmpty()) {
                        EmptyHistoryView()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(history) { loan ->
                                HistoryItem(loan)
                            }
                            item { Spacer(modifier = Modifier.height(100.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "No History Yet",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = appColors.textPrimary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Your salary advance transactions will appear here.",
            color = appColors.textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 40.dp),
            textAlign = TextAlign.Center
        )
    }
}

// Maps a loan status to its visual treatment: accent color + leading icon.
private data class LoanStatusStyle(val color: Color, val icon: ImageVector)

private fun statusStyle(status: String?): LoanStatusStyle = when (status) {
    "REPAID", "DISBURSED" -> LoanStatusStyle(CitySuccess, Icons.Default.CheckCircle)
    "FAILED", "REJECTED" -> LoanStatusStyle(CityError, Icons.Default.Cancel)
    else -> LoanStatusStyle(CityWarning, Icons.Default.Schedule)
}

@Composable
fun HistoryItem(loan: LoanRequestDTO) {
    val style = statusStyle(loan.status)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.surface.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, appColors.divider.copy(alpha = 0.5f))
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
                    overflow = TextOverflow.Ellipsis
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
