package com.robotbot.financetracker.domain.entities

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID

data class TransactionCategoryEntity(
    val transactionType: TransactionType,
    val name: String,
    var id: Int = UNDEFINED_ID
)
