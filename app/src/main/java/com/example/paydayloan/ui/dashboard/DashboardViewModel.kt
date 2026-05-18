package com.example.paydayloan.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paydayloan.api.RetrofitClient
import com.example.paydayloan.api.MockDataRepository
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
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Success(createFallbackDashboard()))
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun loadDashboard(employeeId: Long) {
        // Refresh with latest repository data immediately
        _uiState.value = DashboardUiState.Success(createFallbackDashboard())
        
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getDashboard(employeeId)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        // Merge API data with mock repository state if needed
                        // For this demo, we'll stick to updating from repository
                        _uiState.value = DashboardUiState.Success(createFallbackDashboard())
                    }
                }
            } catch (_: Exception) {
                // Keep showing repository data if network fails
            }
        }
    }

    private fun createFallbackDashboard(): EmployeeDashboardDTO {
        return EmployeeDashboardDTO(
            monthlySalary = 50000.0,
            eligibleAmount = 40000.0,
            availableLimit = 25000.0,
            activeLoan = MockDataRepository.activeLoan,
            loanHistory = MockDataRepository.loanHistory.toList()
        )
    }
}
