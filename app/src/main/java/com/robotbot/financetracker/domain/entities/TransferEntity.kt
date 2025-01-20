package com.robotbot.financetracker.domain.entities

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID
import java.math.BigDecimal
import java.util.Calendar

data class TransferEntity(
    val accountFrom: BankAccountEntity,
    val accountTo: BankAccountEntity,
    val amountFrom: BigDecimal,
    val amountTo: BigDecimal,
    val date: Calendar,
    var id: Int = UNDEFINED_ID
)
