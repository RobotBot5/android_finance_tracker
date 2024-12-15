package com.robotbot.financetracker.domain.entities

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionEntity(
    val transactionType: TransactionType,
    val amount: BigDecimal,
    val category: TransactionCategoryEntity,
    val date: LocalDateTime,
    var id: Int = UNDEFINED_ID
)