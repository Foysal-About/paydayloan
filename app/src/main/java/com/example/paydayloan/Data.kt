package com.example.paydayloan

data class Employee(
    val name: String,
    val salary: Double,
    val eligibleAmount: Double,
    val availableLimit: Double,
    val activeLoan: Loan?,
    val lastLoanHistory: List<Loan>
)

data class Loan(
    val id: String,
    val amount: Double,
    val serviceCharge: Double,
    val netAmount: Double,
    val status: LoanStatus,
    val repaymentDate: String,
    val purpose: String? = null,
    val requestDate: String
)

enum class LoanStatus {
    PENDING_EMPLOYER_APPROVAL,
    APPROVED,
    REJECTED,
    DISBURSED,
    REPAID
}

// Dummy data
val dummyEmployee = Employee(
    name = "Foysal Islam",
    salary = 50000.0,
    eligibleAmount = 40000.0, // 80% of salary
    availableLimit = 40000.0, // assuming no active loan
    activeLoan = null,
    lastLoanHistory = listOf(
        Loan(
            id = "LOAN001",
            amount = 20000.0,
            serviceCharge = 400.0, // 2%
            netAmount = 19600.0,
            status = LoanStatus.REPAID,
            repaymentDate = "2024-04-30",
            purpose = "Medical emergency",
            requestDate = "2024-04-15"
        )
    )
)
