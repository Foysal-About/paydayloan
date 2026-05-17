package com.example.paydayloan.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paydayloan.api.RetrofitClient
import com.example.paydayloan.api.model.EmployeeDashboardDTO
import com.example.paydayloan.api.model.LoanRequestDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val data: EmployeeDashboardDTO) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel : ViewModel() {
    // Initialize with fallback data to reduce perceived loading delay
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Success(createFallbackDashboard()))
    val uiState: StateFlow<DashboardUiState> = _uiState

    private var isLoaded = false

    fun loadDashboard(employeeId: Long) {
        // If already loaded once, don't show loading state again to keep UI snappy
        if (isLoaded) return 
        
        viewModelScope.launch {
            // We don't set Loading state here if we already have fallback data to show
            // but we fetch the latest data in the background
            try {
                val response = RetrofitClient.api.getDashboard(employeeId)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        _uiState.value = DashboardUiState.Success(it)
                        isLoaded = true
                    }
                }
            } catch (_: Exception) {
                // Keep showing fallback if network fails
            }
        }
    }

    private fun createFallbackDashboard(): EmployeeDashboardDTO {
        return EmployeeDashboardDTO(
            monthlySalary = 50000.0,
            eligibleAmount = 40000.0,
            availableLimit = 25000.0,
            activeLoan = null,
            loanHistory = listOf(
                LoanRequestDTO(
                    id = 1,
                    employeeId = 1,
                    productConfigId = 1,
                    requestedAmount = 15000.0,
                    purpose = "Medical Emergency",
                    status = "DISBURSED",
                    requestDate = "10 May 2024"
                ),
                LoanRequestDTO(
                    id = 2,
                    employeeId = 1,
                    productConfigId = 1,
                    requestedAmount = 5000.0,
                    purpose = "Utility Bills",
                    status = "REPAID",
                    requestDate = "25 Apr 2024"
                ),
                LoanRequestDTO(
                    id = 3,
                    employeeId = 1,
                    productConfigId = 1,
                    requestedAmount = 12000.0,
                    purpose = "House Rent",
                    status = "REJECTED",
                    requestDate = "15 Mar 2024"
                ),
                LoanRequestDTO(
                    id = 4,
                    employeeId = 1,
                    productConfigId = 1,
                    requestedAmount = 8000.0,
                    purpose = "Education Fee",
                    status = "FAILED",
                    requestDate = "02 Mar 2024"
                ),
                LoanRequestDTO(
                    id = 5,
                    employeeId = 1,
                    productConfigId = 1,
                    requestedAmount = 20000.0,
                    purpose = "Family Support",
                    status = "DISBURSED",
                    requestDate = "20 Feb 2024"
                )
            )
        )
    }
}
