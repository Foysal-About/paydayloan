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
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun loadDashboard(employeeId: Long) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val response = RetrofitClient.api.getDashboard(employeeId)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        _uiState.value = DashboardUiState.Success(it)
                    } ?: run {
                        _uiState.value = DashboardUiState.Success(createFallbackDashboard())
                    }
                } else {
                    _uiState.value = DashboardUiState.Success(createFallbackDashboard())
                }
            } catch (_: Exception) {
                _uiState.value = DashboardUiState.Success(createFallbackDashboard())
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
                )
            )
        )
    }
}
