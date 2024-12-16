package com.robotbot.financetracker.domain.entities

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID
import java.math.BigDecimal

data class BankAccountEntity(
    val name: String,
    val balance: BigDecimal,
    val currency: Currency,
    val transactions: List<TransactionEntity> = listOf(),
    val transfers: List<TransferEntity> = listOf(),
    var id: Int = UNDEFINED_ID
)
