package com.example.paydayloan.ui.applyadvance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paydayloan.api.RetrofitClient
import com.example.paydayloan.api.model.LoanRequestDTO
import com.example.paydayloan.api.model.LoanSimulationDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoanUiState {
    object Idle : LoanUiState()
    object Loading : LoanUiState()
    data class SimulationSuccess(val data: LoanSimulationDTO) : LoanUiState()
    data class RequestSuccess(val data: LoanRequestDTO) : LoanUiState()
    data class Error(val message: String) : LoanUiState()
}

class LoanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoanUiState>(LoanUiState.Idle)
    val uiState: StateFlow<LoanUiState> = _uiState

    fun simulateLoan(employeeId: Long, amount: Double) {
        viewModelScope.launch {
            _uiState.value = LoanUiState.Loading
            try {
                val response = RetrofitClient.api.simulateLoan(employeeId, 1L, amount)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        _uiState.value = LoanUiState.SimulationSuccess(it)
                    }
                } else {
                    _uiState.value = LoanUiState.Error(response.body()?.message ?: "Simulation failed")
                }
            } catch (e: Exception) {
                _uiState.value = LoanUiState.Error(e.localizedMessage ?: "Error during simulation")
            }
        }
    }

    fun applyLoan(employeeId: Long, amount: Double, purpose: String) {
        viewModelScope.launch {
            _uiState.value = LoanUiState.Loading
            try {
                val request = LoanRequestDTO(
                    employeeId = employeeId,
                    productConfigId = 1L,
                    requestedAmount = amount,
                    purpose = purpose
                )
                val response = RetrofitClient.api.requestLoan(request, "1")
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        _uiState.value = LoanUiState.RequestSuccess(it)
                    }
                } else {
                    _uiState.value = LoanUiState.Error(response.body()?.message ?: "Request failed")
                }
            } catch (e: Exception) {
                _uiState.value = LoanUiState.Error(e.localizedMessage ?: "Error during request")
            }
        }
    }
}
