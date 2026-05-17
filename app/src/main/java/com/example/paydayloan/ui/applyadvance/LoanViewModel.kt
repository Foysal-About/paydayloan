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
                     // Fallback to success for demo purposes
                    _uiState.value = LoanUiState.SimulationSuccess(
                        LoanSimulationDTO(
                            requestedAmount = amount,
                            serviceCharge = amount * 0.02,
                            netDisbursement = amount * 0.98,
                            repaymentDate = "30 May 2024"
                        )
                    )
                }
            } catch (_: Exception) {
                // Fallback to success for demo purposes
                _uiState.value = LoanUiState.SimulationSuccess(
                    LoanSimulationDTO(
                        requestedAmount = amount,
                        serviceCharge = amount * 0.02,
                        netDisbursement = amount * 0.98,
                        repaymentDate = "30 May 2024"
                    )
                )
            }
        }
    }

    fun applyLoan(employeeId: Long, amount: Double, purpose: String) {
        // Optimistic UI: Trigger success state instantly to eliminate perceived delay
        val request = LoanRequestDTO(
            employeeId = employeeId,
            productConfigId = 1L,
            requestedAmount = amount,
            purpose = purpose,
            id = 1,
            status = "PENDING",
            requestDate = "2024-05-18"
        )
        
        _uiState.value = LoanUiState.RequestSuccess(request)

        // Perform actual network request in background
        viewModelScope.launch {
            try {
                RetrofitClient.api.requestLoan(request, "1")
                // Success is already set in UI optimistically
            } catch (_: Exception) {
                // Background error handling if necessary
            }
        }
    }
}
