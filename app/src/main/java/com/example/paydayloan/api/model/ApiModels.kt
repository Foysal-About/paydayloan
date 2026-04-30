package com.example.paydayloan.api.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String?
)

data class EmployeeDashboardDTO(
    @SerializedName("monthlySalary") val monthlySalary: Double,
    @SerializedName("eligibleAmount") val eligibleAmount: Double,
    @SerializedName("availableLimit") val availableLimit: Double,
    @SerializedName("activeLoan") val activeLoan: ActiveLoanDTO?,
    @SerializedName("loanHistory") val loanHistory: List<LoanRequestDTO>
)

data class LoanRequestDTO(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("employeeId") val employeeId: Long,
    @SerializedName("productConfigId") val productConfigId: Long,
    @SerializedName("requestedAmount") val requestedAmount: Double,
    @SerializedName("purpose") val purpose: String?,
    @SerializedName("status") val status: String? = null,
    @SerializedName("requestDate") val requestDate: String? = null
)

data class ActiveLoanDTO(
    @SerializedName("loanRefNo") val loanRefNo: String,
    @SerializedName("sanctionedAmount") val sanctionedAmount: Double,
    @SerializedName("outstandingAmount") val outstandingAmount: Double,
    @SerializedName("maturityDate") val maturityDate: String
)

data class LoanSimulationDTO(
    @SerializedName("requestedAmount") val requestedAmount: Double,
    @SerializedName("serviceCharge") val serviceCharge: Double,
    @SerializedName("netDisbursement") val netDisbursement: Double,
    @SerializedName("repaymentDate") val repaymentDate: String
)

data class EmployeeProfileDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?
)
