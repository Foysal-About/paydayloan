package com.example.paydayloan.api

import com.example.paydayloan.api.model.ActiveLoanDTO
import com.example.paydayloan.api.model.LoanRequestDTO

object MockDataRepository {
    var activeLoan: ActiveLoanDTO? = null
    val loanHistory = mutableListOf(
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

    fun applyLoan(amount: Double, purpose: String) {
        val newLoan = LoanRequestDTO(
            id = (loanHistory.size + 1).toLong(),
            employeeId = 1,
            productConfigId = 1,
            requestedAmount = amount,
            purpose = purpose,
            status = "PENDING",
            requestDate = "18 May 2024"
        )
        loanHistory.add(0, newLoan)
        
        // Also set it as active for dashboard
        activeLoan = ActiveLoanDTO(
            loanRefNo = "PDL-2024-001",
            sanctionedAmount = amount,
            outstandingAmount = amount,
            maturityDate = "30 May 2024"
        )
    }
}
