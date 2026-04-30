package com.example.paydayloan.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paydayloan.api.RetrofitClient
import com.example.paydayloan.api.model.EmployeeDashboardDTO
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
                        _uiState.value = DashboardUiState.Error("Empty data received")
                    }
                } else {
                    _uiState.value = DashboardUiState.Error(response.body()?.message ?: "Failed to load dashboard")
                }
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }
}
