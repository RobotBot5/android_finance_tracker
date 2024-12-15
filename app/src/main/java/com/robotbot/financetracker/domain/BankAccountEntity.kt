package com.robotbot.financetracker.domain

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID
import java.math.BigDecimal

data class BankAccountEntity(
    val name: String,
    val balance: BigDecimal,
    val currency: Currency,
    val transactions: List<TransactionEntity>,
    val transfers: List<TransferEntity>,
    var id: Int = UNDEFINED_ID
)
