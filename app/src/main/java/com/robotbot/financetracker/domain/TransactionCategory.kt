package com.robotbot.financetracker.domain

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID

data class TransactionCategory(
    val transactionType: TransactionType,
    val name: String,
    var id: Int = UNDEFINED_ID
)
