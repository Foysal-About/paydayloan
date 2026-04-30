package com.example.paydayloan.api

import com.example.paydayloan.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface PayDayLoanApi {
    @GET("loans/health")
    suspend fun healthCheck(): Response<ApiResponse<String>>

    @GET("loans/employee/{id}/dashboard")
    suspend fun getDashboard(
        @Path("id") employeeId: Long,
        @Header("X-User-Id") userIdHeader: String? = null
    ): Response<ApiResponse<EmployeeDashboardDTO>>

    @GET("loans/simulate")
    suspend fun simulateLoan(
        @Query("employeeId") employeeId: Long,
        @Query("productConfigId") productConfigId: Long,
        @Query("requestedAmount") requestedAmount: Double
    ): Response<ApiResponse<LoanSimulationDTO>>

    @POST("loans/request")
    suspend fun requestLoan(
        @Body request: LoanRequestDTO,
        @Header("X-User-Id") userIdHeader: String? = "1"
    ): Response<ApiResponse<LoanRequestDTO>>

    @GET("loans/requests")
    suspend fun getLoanHistory(
        @Query("employeeId") employeeId: Long
    ): Response<ApiResponse<List<LoanRequestDTO>>>

    @GET("loans/employee/{id}/active")
    suspend fun getActiveLoans(
        @Path("id") employeeId: Long
    ): Response<ApiResponse<List<ActiveLoanDTO>>>

    @GET("employees/{id}")
    suspend fun getProfile(
        @Path("id") employeeId: Long
    ): Response<ApiResponse<EmployeeProfileDTO>>
}
